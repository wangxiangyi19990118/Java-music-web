package Service;

import Bean.MusicBean;
import Bean.MusicSheetBean;
import Bean.UserBean;
import Mongo.Jdbc;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.bson.Document;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MusicsheetService {
	private MongoCollection<Document> collection_musicsheet;
	private MongoCollection<Document> collection_user;
	private MongoCollection<Document> collection_music;
	private MongoCollection<Document> collection_comment;

	public MusicsheetService() {
	}

	public MusicsheetService(MongoCollection<Document> collection_musicsheet, MongoCollection<Document> collection_user,
                             MongoCollection<Document> collection_music, MongoCollection<Document> collection_comment) {
		this.collection_musicsheet = collection_musicsheet;
		this.collection_user = collection_user;
		this.collection_music = collection_music;
		this.collection_comment = collection_comment;
	}

	/**
	 * 创建歌单
	 * 
	 * @param request
	 * @return
	 */
	public boolean create(HttpServletRequest request) {
		

		// querystring should do url decode!
		// request 中 url qurystirng should do url decode.
		// referance:http://blog.csdn.net/lian_zhihui1984/article/details/6822201
		// http://blog.csdn.net/a352193394/article/details/7477041
		// 判断enctype属性是否为multipart/form-data

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload
				upload = new ServletFileUpload(factory);

		// 设置上传内容的大小限制（单位：字节）
		upload.setSizeMax(5 * 1024 * 1024);

		// Parse the request
		List<?> items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}

		String picMd5 = "";
		String numberId = "";
		String sheetName = "";
		String picUrl = "";
		Iterator<?> iter = items.iterator();

		while (iter.hasNext()) {
			
			FileItem item = (FileItem) iter.next();

			if (item.isFormField()) {
				// 如果是普通表单字段
				String name = item.getFieldName();
				String value = "";
				try {
					value = item.getString("UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				switch (name) {
				case "NumberId":
					numberId = value;
					break;
				case "SheetName":
					sheetName = value;
				}
			} else {
				// 如果是文件字段
				// String fieldName = item.getFieldName();
				String value = item.getName();// 会将完整路径名传过来
				String suffix = value.substring(value.lastIndexOf(".") + 1);
				
				OutputStream out = null;
				InputStream in = null;
				try {
					in = item.getInputStream();
					//picMd5 = DigestUtils.md5Hex(IOUtils.toByteArray(in));
					ServletContext sct = request.getServletContext();
					String serverSavaPath = request.getServletContext().getRealPath("/")+sct.getInitParameter("PictureFilePath").toString();
					picUrl = picMd5 + "." + suffix;
					out = new FileOutputStream(new File(serverSavaPath, picUrl));
					int length = 0;
					byte[] buf = new byte[1024];
					in.close();
					//这里必须重复获取 输入流，因为上面的hash操作会将输入流指针移到末尾
					in=item.getInputStream();
					while ((length = in.read(buf)) != -1) {
						out.write(buf, 0, length);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				} finally {
					// close stream
					try {
						if (in != null)
							in.close();
						if (out != null)
							out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}

		

		// 创建歌单
		MusicSheetBean musicSheet = new MusicSheetBean();
		musicSheet.setName(sheetName);
		musicSheet.setPicUrl(picUrl);
		Jdbc .insert(collection_musicsheet, musicSheet);

		// 更新用户歌单关联
		UserBean user = Jdbc .findOne(collection_user, eq("numberid", numberId), new TypeReference<UserBean>() {
		});
		if (user != null) {
			user.getMusicsheets().add(musicSheet.get_id());
			return Jdbc .update(collection_user, user);
		}
		return false;

	}

	/**
	 * 删除歌单
	 * 
	 * @param HttpServletRequest
	 * @return
	 */
	public boolean delete(HttpServletRequest request) {
		String musicsheetId = request.getParameter("id");
		MusicSheetBean musicSheet = Jdbc .findOne(collection_musicsheet, eq("_id", musicsheetId),
				new TypeReference<MusicSheetBean>() {
				});
		if (musicSheet == null)
			return false;
		else {
			UserBean user=Jdbc.findOne(collection_user, eq("musicsheets",musicsheetId),new TypeReference<UserBean>() {});
			user.getMusicsheets().remove(musicsheetId);
			Jdbc.delete(collection_musicsheet, musicSheet);
			return Jdbc.update(collection_user, user);
		}
		
	}

	/**
	 * 获取 歌单集合大小
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public int getMusicsheetCount() {
		int total = 0;
		for (Document doc : collection_musicsheet.find())
			total++;
		return total;
	}

	/***
	 * 获取某一页歌单
	 * 
	 * @param currentPage
	 * @param perSize
	 * @return 前端规定格式
	 */
	public List<MusicSheetPack> getAllMusicsheets(HttpServletRequest request, int perSize) {
		int currtenPage = Integer.parseInt(request.getParameter("current"));
		List<MusicSheetPack> ansls = new LinkedList<MusicSheetPack>();

		if (currtenPage < 0)
			return ansls;
		MusicSheetBean ms = null;
		MusicSheetPack msp = null;
		int cnt=0;
		FindIterable<Document> col = collection_musicsheet.find().skip(currtenPage * perSize);
		try {
			for (Document doc : col) {
				if(++cnt>perSize)
					break;
				ms = JSON.parseObject(doc.toJson(), new TypeReference<MusicSheetBean>() {
				});
				msp=new MusicSheetPack();
				msp.setId(ms.get_id());
				msp.setName(ms.getName());
				msp.setPictureUrl(ms.getPicUrl());
				msp.setCreateTime(ms.getCreateTime());
				msp.setTotalSongs(String.valueOf(ms.getMusics().size()));
				ansls.add(msp);
			}
			return ansls;
		} catch (Exception e) {
			// TODO: handle exception
			return ansls;
		}
	}

	/***
	 * 获取某人的所有歌单
	 * 
	 * @param userid
	 * @return
	 */
	public List<MusicSheetPack> getMusicsheetsByUserid(HttpServletRequest request) {
		String numberId = request.getParameter("userid");
		List<MusicSheetPack> ansls = new LinkedList<MusicSheetPack>();
		UserBean user = Jdbc .findOne(collection_user, eq("numberid", numberId), new TypeReference<UserBean>() {
		});
		if (user != null) {
			MusicSheetBean ms = null;
			MusicSheetPack msp = null;
			List<String> msids = user.getMusicsheets();
			for (String string : msids) {
//				ms = MongoUtil.findOne(collection_musicsheet, eq("_id", string), new TypeReference<MusicSheet>() {
//				});
				ms = Jdbc .findOne(collection_musicsheet, eq("_id", string),
						new TypeReference<MusicSheetBean>() {
						});
				
				msp = new MusicSheetPack();
				msp.setId(ms.get_id());
				msp.setName(ms.getName());
				msp.setCreateTime(ms.getCreateTime());
				msp.setPictureUrl(ms.getPicUrl());
				msp.setTotalSongs(String.valueOf(ms.getMusics().size()));
				ansls.add(msp);
			}
		}
		return ansls;
	}

	/***
	 * 获取歌单的歌曲
	 * 
	 * @param musicsheetid
	 * @return
	 */
	public List<MusicBean> getMusics(HttpServletRequest request) {
		List<MusicBean> anList = new LinkedList<MusicBean>();
		String msid = request.getParameter("musicsheetId");
		MusicSheetBean ms = Jdbc .findOne(collection_musicsheet, eq("_id", msid), new TypeReference<MusicSheetBean>() {
		});
		if (ms != null) {
			List<String> mids = ms.getMusics();
			for (String string : mids) {
				anList.add(Jdbc .findOne(collection_music, eq("_id", string), new TypeReference<MusicBean>() {
				}));
			}
		}
		return anList;
	}



	class MusicSheetPack {
		private String id;
		private String name;
		private String pictureUrl;
		private String createTime;
		private String totalSongs;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPictureUrl() {
			return pictureUrl;
		}

		public void setPictureUrl(String pictureUrl) {
			this.pictureUrl = pictureUrl;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getTotalSongs() {
			return totalSongs;
		}

		public void setTotalSongs(String totalSongs) {
			this.totalSongs = totalSongs;
		}

		@Override
		public String toString() {
			return "MusicSheetPack [id=" + id + ", name=" + name + ", pictureUrl=" + pictureUrl + ", createTime="
					+ createTime + ", totalSongs=" + totalSongs + "]";
		}
	}
}
