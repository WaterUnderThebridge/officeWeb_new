package com.tlgc.utils.msg.lib.base;

import com.tlgc.utils.msg.entity.DataStore;

public abstract class SenderWapper {

	protected DataStore requestData = new DataStore();

	public abstract ISender getSender();
}
