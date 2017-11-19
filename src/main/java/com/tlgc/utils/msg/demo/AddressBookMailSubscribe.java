package com.tlgc.utils.msg.demo;

import com.tlgc.utils.msg.config.AppConfig;
import com.tlgc.utils.msg.lib.ADDRESSBOOKMail;
import com.tlgc.utils.msg.utils.ConfigLoader;

public class AddressBookMailSubscribe {

	public static void main(String[] args) {

		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Mail);
		ADDRESSBOOKMail addressbook = new ADDRESSBOOKMail(config);
		addressbook.setAddress("leo@apple.cn", "leo");
		addressbook.subscribe();
	}	
}
