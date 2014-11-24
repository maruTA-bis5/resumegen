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
package net.bis5.resumegen.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.bis5.resumegen.util.DateTimeUtil;

/**
 * @author maruTA_bis5
 */
public class EduBgCareerRapTableModel {

    private Calendar yearMonth = null;

    private String content;

    /**
     * @param yearMonth
     * @param content
     */
    public EduBgCareerRapTableModel( Date yearMonth, String content) {
        if ( yearMonth != null) {
            Calendar yearMonthCal = GregorianCalendar.getInstance();
            yearMonthCal.setTime( yearMonth);
            this.yearMonth = yearMonthCal;
        }
        this.content = content;
    }

    public EduBgCareerRapTableModel( String yearMonth, String content) {
        // yearMonthが文字列で来るとパース面倒・・・Dateにするくらいなら年と月を別々にして送る方が賢い設計
        this( DateTimeUtil.parseDate( yearMonth), content);
    }

    public Integer getYear() {
        if ( yearMonth == null) {
            return null;
        }
        return yearMonth.get( Calendar.YEAR);
    }

    public Integer getMonth() {
        if ( yearMonth == null) {
            return null;
        }
        return yearMonth.get( Calendar.MONTH);
    }

    public String getContent() {
        return content;
    }

}
