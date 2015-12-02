package asmack.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户类,包含 jid，nickname，和 在线状态，个性签名。
 * @author huajian.zhang
 *
 */
public class UserVo implements Parcelable{
	private String userId; // 用户全名，如：user001@aji，  JID
	/**在线available和离线unavailable*/
	private String type;// 用户类型  从presence的Type读取 ， 或者是从RosterPacket.Item 查看相关 ItemStatue的相关字段辅助理解。， 我只需要用到他的 在线available和离线unavailable 2种状态。 
	private String status;// 用户签名，如：在线，离开、出去吃饭去了等   汉字描述, 从 presence类的status读取
	
	//下面字段来自 RosterEntry
	private String nickName; //昵称
	private String itemType;  //从 RosterEntry中取
	private String itemStatus; //从RosterEntry中取
	
//	private RosterEntry rosterEntry; //这个类中的name 是nickename，注意。

	public UserVo() {
	}
	
	public UserVo(String userId, String type, String status) {
		this.userId = userId;
		this.type = type;
		this.status = status;
	}

	public UserVo(String userId, String userName, String type, String status,
			String nickName, String itemType, String itemStatus) {
		this.userId = userId;
		this.type = type;
		this.status = status;
		this.nickName = nickName;
		this.itemType = itemType;
		this.itemStatus = itemStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserVo other = (UserVo) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

     public int describeContents() {
         return 0;
     }

     public void writeToParcel(Parcel out, int flags) {
         out.writeString(userId);
         out.writeString(type);
         out.writeString(status);
         out.writeString(nickName);
         out.writeString(itemType);
         out.writeString(itemStatus);
     }

     public static final Parcelable.Creator<UserVo> CREATOR  = new Parcelable.Creator<UserVo>() {
         public UserVo createFromParcel(Parcel in) {
             return new UserVo(in);
         }

         public UserVo[] newArray(int size) {
             return new UserVo[size];
         }
     };
     
     private UserVo(Parcel in) {
    	 userId = in.readString();
    	 type = in.readString();
    	 status = in.readString();
    	 nickName = in.readString();
    	 itemType = in.readString();
    	 itemStatus = in.readString();
     }

	@Override
	public String toString() {
		return "UserVo [userId=" + userId + ", type=" + type + ", status="
				+ status + ", nickName=" + nickName + ", itemType=" + itemType
				+ ", itemStatus=" + itemStatus + "]";
	}

	
	

}
