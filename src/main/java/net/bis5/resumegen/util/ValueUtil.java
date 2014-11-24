/**
 * Copyright (C) 2014 maruTA_bis5
 *     http://entrance.bis5.net
 *     http://github.com/bis5
 * 
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bis5.resumegen.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author maruTA_bis5
 */
public class ValueUtil {

	private ValueUtil() {
	}

	public static String killNull(String nullable) {
		return nullable != null ? nullable : "";
	}

	public static <T, R extends T> T killNull(T nullable, R whenNull) {
		return nullable != null ? nullable : whenNull;
	}

	private static final Map<String, String> GENDER_MAP;
	static {
		Map<String, String> map = new HashMap<String, String>();
		map.put(null, "");
		map.put("male", "男");
		map.put("female", "女");
		GENDER_MAP = Collections.unmodifiableMap(map);
	}

	public static String getGender(String gender) {
		return GENDER_MAP.get(gender);
	}

	private static final Map<String, String> EXIST_NONEXIST_MAP;
	static {
		Map<String, String> map = new HashMap<String, String>();
		map.put(null, "");
		map.put("true", "有");
		map.put("false", "無");
		EXIST_NONEXIST_MAP = Collections.unmodifiableMap(map);
	}

	public static String getExistNonExist(String isExist) {
		return EXIST_NONEXIST_MAP.get(isExist);
	}

}
