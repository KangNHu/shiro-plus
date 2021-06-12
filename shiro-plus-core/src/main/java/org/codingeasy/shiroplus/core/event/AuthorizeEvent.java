package org.codingeasy.shiroplus.core.event;

import java.util.EventObject;

/**
* 授权事件  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class AuthorizeEvent extends EventObject {

	private CommonEventType type;
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public AuthorizeEvent(Object source  , CommonEventType type) {
		super(source);
		this.type = type;
	}


	public CommonEventType getType() {
		return type;
	}


}
