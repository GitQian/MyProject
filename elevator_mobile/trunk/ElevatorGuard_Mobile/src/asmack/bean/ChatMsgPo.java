package asmack.bean;
/**
 * 聊天信息本地数据库中存储。
 * @author liucheng  liucheng187@qq.com
 */
public class ChatMsgPo {
	private long _id;
	private String _from; //--消息发送方 jid的 username那部分
	private String _to;  //消息接收方
	private String _body; //--消息文字内容 
	private long _time;   //--消息接受时的时间，System.currentTime()
	private int _state;   //--该条消息是否已读，0表示未读(false),1表示已读(true)
	private int _type;    //--type类型决定是 line-by-line单聊还是 group chats群聊, 0表示单聊，1表示群聊
	private int _msgType;  //--消息类型  1：inbox 2：sent 3：draft
	
	public ChatMsgPo() {
		// TODO Auto-generated constructor stub
	}
	
	public ChatMsgPo(long _id, String _from, String _to, String _body,
			long _time, int _state, int _type, int _msgType) {
		this._id = _id;
		this._from = _from;
		this._to = _to;
		this._body = _body;
		this._time = _time;
		this._state = _state;
		this._type = _type;
		this._msgType = _msgType;
	}



	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String get_from() {
		return _from;
	}

	public void set_from(String _from) {
		this._from = _from;
	}

	public String get_to() {
		return _to;
	}

	public void set_to(String _to) {
		this._to = _to;
	}

	public String get_body() {
		return _body;
	}

	public void set_body(String _body) {
		this._body = _body;
	}

	public long get_time() {
		return _time;
	}

	public void set_time(long _time) {
		this._time = _time;
	}

	public int get_state() {
		return _state;
	}

	public void set_state(int _state) {
		this._state = _state;
	}

	public int get_type() {
		return _type;
	}

	public void set_type(int _type) {
		this._type = _type;
	}

	public int get_msgType() {
		return _msgType;
	}

	public void set_msgType(int _msgType) {
		this._msgType = _msgType;
	}
	
	
}
