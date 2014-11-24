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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author maruTA_bis5
 */
public class DateTimeUtil {

	private DateTimeUtil() {
	}

	public static Date parseDate(String strDate) {
		try {
			return SimpleDateFormatConst.YYYYMMDD_HYPHEN_SEPARATOR.parse(strDate);
		} catch (ParseException | NullPointerException e) {
			return GregorianCalendar.getInstance().getTime();
		}
	}

	public static int calcAgeAt(Date birthday, Date targetDate) {
		if (birthday == null || targetDate == null) {
			return 0;
		} else if (birthday.after(targetDate)) { // 負数はとりあえず0扱いしたい
			return 0;
		}
		Calendar birth = Calendar.getInstance();
		birth.setTime(birthday);
		Calendar target = Calendar.getInstance();
		target.setTime(targetDate);
		int yearDiff = birth.get(Calendar.YEAR) - target.get(Calendar.YEAR);
		if (birth.get(Calendar.MONTH) > target.get(Calendar.MONTH)) {
			yearDiff--;
		} else if (birth.get(Calendar.DAY_OF_MONTH) > target.get(Calendar.DAY_OF_MONTH)) {
			yearDiff--;
		}
		return yearDiff;
	}
}
