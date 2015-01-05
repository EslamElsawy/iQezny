package com.badrit.iqezny.models;

public class Friend {

	public String friendID;
	public String friendName;
	public boolean isSleep;
	public boolean isZomaraUser;

	public Friend(String friendID, String friendName, boolean isSleep, boolean isZomaraUser) {
		super();
		this.friendID = friendID;
		this.friendName = friendName;
		this.isSleep = isSleep;
		this.isZomaraUser = isZomaraUser;
	}

}
