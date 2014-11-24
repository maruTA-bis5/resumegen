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
package net.bis5.resumegen.component;

import static net.bis5.resumegen.component.RirekigenParamKey.AGE;
import static net.bis5.resumegen.component.RirekigenParamKey.BIRTHDAY;
import static net.bis5.resumegen.component.RirekigenParamKey.CAREER_CNT;
import static net.bis5.resumegen.component.RirekigenParamKey.CAREER_PREFIX;
import static net.bis5.resumegen.component.RirekigenParamKey.COMMUTING_TIME;
import static net.bis5.resumegen.component.RirekigenParamKey.DESIRE;
import static net.bis5.resumegen.component.RirekigenParamKey.EDU_BG_CNT;
import static net.bis5.resumegen.component.RirekigenParamKey.EDU_BG_PREFIX;
import static net.bis5.resumegen.component.RirekigenParamKey.GENDER;
import static net.bis5.resumegen.component.RirekigenParamKey.HAS_SPOUSE;
import static net.bis5.resumegen.component.RirekigenParamKey.HAS_SPOUSE_DEPENDENT;
import static net.bis5.resumegen.component.RirekigenParamKey.IS_RAP_ENABLED;
import static net.bis5.resumegen.component.RirekigenParamKey.LICENSE_CNT;
import static net.bis5.resumegen.component.RirekigenParamKey.LICENSE_PREFIX;
import static net.bis5.resumegen.component.RirekigenParamKey.MAIN_ADDRESS;
import static net.bis5.resumegen.component.RirekigenParamKey.MAIN_ADDRESS_KANA;
import static net.bis5.resumegen.component.RirekigenParamKey.MAIN_ADDRESS_POSTAL;
import static net.bis5.resumegen.component.RirekigenParamKey.MAIN_TEL;
import static net.bis5.resumegen.component.RirekigenParamKey.NAME;
import static net.bis5.resumegen.component.RirekigenParamKey.NAME_KANA;
import static net.bis5.resumegen.component.RirekigenParamKey.NUM_FAMILY;
import static net.bis5.resumegen.component.RirekigenParamKey.PR;
import static net.bis5.resumegen.component.RirekigenParamKey.RAP_CNT;
import static net.bis5.resumegen.component.RirekigenParamKey.RAP_PREFIX;
import static net.bis5.resumegen.component.RirekigenParamKey.REPORT_DATE;
import static net.bis5.resumegen.component.RirekigenParamKey.SUB_ADDRESS;
import static net.bis5.resumegen.component.RirekigenParamKey.SUB_ADDRESS_KANA;
import static net.bis5.resumegen.component.RirekigenParamKey.SUB_ADDRESS_POSTAL;
import static net.bis5.resumegen.component.RirekigenParamKey.SUB_TEL;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.bis5.resumegen.entity.EduBgCareerRapTableModel;
import net.bis5.resumegen.util.DateTimeUtil;
import net.bis5.resumegen.util.ValueUtil;

/**
 * @author maruTA_bis5
 */
public class ParamParser {

    private Map<String, String> baseParams;

    public ParamParser( Map<String, String> baseParams) {
        this.baseParams = Collections.unmodifiableMap( baseParams);
    }

    public Map<RirekigenParamKey, Object> parse() {
        Map<RirekigenParamKey, Object> parsedParams = new HashMap<RirekigenParamKey, Object>();

        // 出力日
        Date reportDate = DateTimeUtil.parseDate( baseParams.get( REPORT_DATE.key()));
        parsedParams.put( REPORT_DATE, reportDate);
        // 氏名
        parsedParams.put( NAME, baseParams.get( NAME.key()));
        // 氏名カナ
        parsedParams.put( NAME_KANA, baseParams.get( NAME_KANA.key()));
        // 性別
        parsedParams.put( GENDER, ValueUtil.getGender( baseParams.get( GENDER.key())));
        // 生年月日
        Date birthday = DateTimeUtil.parseDate( baseParams.get( BIRTHDAY.key()));
        parsedParams.put( BIRTHDAY, birthday);
        // 年齢
        parsedParams.put( AGE, DateTimeUtil.calcAgeAt( birthday, reportDate));
        // 主住所
        parsedParams.put( MAIN_ADDRESS, baseParams.get( MAIN_ADDRESS.key()));
        // 主住所カナ
        parsedParams.put( MAIN_ADDRESS_KANA, baseParams.get( MAIN_ADDRESS_KANA.key()));
        // 主住所郵便番号
        parsedParams.put( MAIN_ADDRESS_POSTAL, baseParams.get( MAIN_ADDRESS_POSTAL.key()));
        // 副住所
        parsedParams.put( SUB_ADDRESS, baseParams.get( SUB_ADDRESS.key()));
        // 副住所カナ
        parsedParams.put( SUB_ADDRESS_KANA, baseParams.get( SUB_ADDRESS_KANA.key()));
        // 副住所郵便番号
        parsedParams.put( SUB_ADDRESS_POSTAL, baseParams.get( SUB_ADDRESS_POSTAL.key()));
        // 主電話番号
        parsedParams.put( MAIN_TEL, baseParams.get( MAIN_TEL.key()));
        // 副電話番号
        parsedParams.put( SUB_TEL, baseParams.get( SUB_TEL.key()));
        // 学歴、職歴、賞罰 TODO どうしようね・・・
        parsedParams.putAll( generateEduBgCareerRapParams());
        // 資格・免許 TODO どうしようね・・・
        parsedParams.putAll( generateLicenseParams());
        // 志望の動機・特技・好きな学科・アピールポイントなど
        parsedParams.put( PR, baseParams.get( PR.key()));
        // 通勤時間
        parsedParams.put( COMMUTING_TIME, baseParams.get( COMMUTING_TIME.key()));
        // 扶養家族数（配偶者を除く）
        parsedParams.put( NUM_FAMILY, baseParams.get( NUM_FAMILY.key()));
        // 配偶者の有無
        parsedParams.put( HAS_SPOUSE, ValueUtil.getExistNonExist( baseParams.get( HAS_SPOUSE.key())));
        // 配偶者扶養義務
        parsedParams.put( HAS_SPOUSE_DEPENDENT, ValueUtil.getExistNonExist( baseParams.get( HAS_SPOUSE_DEPENDENT.key())));
        // 本人希望記入欄
        parsedParams.put( DESIRE, baseParams.get( DESIRE.key()));

        return parsedParams;
    }

    /**
     * @return
     */
    private Map<? extends RirekigenParamKey, ? extends Object> generateLicenseParams() {
        Map<RirekigenParamKey, Object> params = new HashMap<RirekigenParamKey, Object>();
        int cnt = Integer.valueOf( baseParams.get( LICENSE_CNT.key()));
        List<EduBgCareerRapTableModel> models = new LinkedList<EduBgCareerRapTableModel>();
        for ( int i = 1; i <= cnt; i++) {
            final String yearMonthKey = LICENSE_PREFIX.key() + "YearMonth" + i;
            final String contKey = LICENSE_PREFIX.key() + "Content" + i;
            EduBgCareerRapTableModel model = new EduBgCareerRapTableModel( baseParams.get( yearMonthKey), baseParams.get( contKey));
            models.add( model);
        }
        params.put( LICENSE_PREFIX, models);
        return params;
    }

    private Map<RirekigenParamKey, Object> generateEduBgCareerRapParams() {
        Map<RirekigenParamKey, Object> params = new HashMap<RirekigenParamKey, Object>();
        final int maxRows = 22 - 2 - 2; // 全行数 - 学歴ヘッダ・フッタ - 職歴ヘッダ・フッタ
        int usedRowCnt = maxRows;

        // 学歴
        {
            List<EduBgCareerRapTableModel> eduBgParams = new LinkedList<EduBgCareerRapTableModel>();
            int eduBgCnt = Integer.valueOf( baseParams.get( EDU_BG_CNT.key()));
            if ( eduBgCnt == 0) {
                eduBgParams.addAll( Arrays.asList( new EduBgCareerRapTableModel( ( Date) null, "学歴"), new EduBgCareerRapTableModel( ( Date) null,
                    "無し")));
            }
            else {
                List<EduBgCareerRapTableModel> models = new LinkedList<EduBgCareerRapTableModel>();
                models.add( new EduBgCareerRapTableModel( ( Date) null, "学歴"));
                for ( int i = 1; i <= eduBgCnt; i++) {
                    final String yearMonthKey = EDU_BG_PREFIX.key() + "YearMonth" + i;
                    final String contKey = EDU_BG_PREFIX.key() + "Content" + i;
                    EduBgCareerRapTableModel model = new EduBgCareerRapTableModel( baseParams.get( yearMonthKey), baseParams.get( contKey));
                    models.add( model);
                }
                models.add( new EduBgCareerRapTableModel( ( Date) null, "以上"));
                eduBgParams.addAll( models);
            }
            usedRowCnt -= eduBgCnt + 2;
            params.put( EDU_BG_PREFIX, eduBgParams);
        }
        // 職歴
        {
            List<EduBgCareerRapTableModel> careerParams = new LinkedList<EduBgCareerRapTableModel>();
            int careerCnt = Integer.valueOf( baseParams.get( CAREER_CNT.key()));
            if ( careerCnt == 0) {
                careerParams.addAll( Arrays.asList( new EduBgCareerRapTableModel( ( Date) null, "職歴"), new EduBgCareerRapTableModel( ( Date) null,
                    "無し")));
            }
            else {
                List<EduBgCareerRapTableModel> models = new LinkedList<EduBgCareerRapTableModel>();
                models.add( new EduBgCareerRapTableModel( ( Date) null, "職歴"));
                for ( int i = 1; i <= careerCnt; i++) {
                    final String yearMonthKey = CAREER_PREFIX.key() + "YearMonth" + i;
                    final String contKey = CAREER_PREFIX.key() + "Content" + i;
                    EduBgCareerRapTableModel model = new EduBgCareerRapTableModel( baseParams.get( yearMonthKey), baseParams.get( contKey));
                    models.add( model);
                }
                models.add( new EduBgCareerRapTableModel( ( Date) null, "以上"));
                careerParams.addAll( models);
            }
            usedRowCnt -= careerCnt + 2;
            params.put( CAREER_PREFIX, careerParams);
        }
        // 賞罰
        if ( Boolean.valueOf( baseParams.get( IS_RAP_ENABLED.key()))) {
            List<EduBgCareerRapTableModel> rapParams = new LinkedList<EduBgCareerRapTableModel>();
            int rapCnt = Integer.valueOf( baseParams.get( RAP_CNT.key()));
            if ( rapCnt == 0) {
                rapParams.addAll( Arrays
                    .asList( new EduBgCareerRapTableModel( ( Date) null, "賞罰"), new EduBgCareerRapTableModel( ( Date) null, "無し")));
            }
            else {
                List<EduBgCareerRapTableModel> models = new LinkedList<EduBgCareerRapTableModel>();
                models.add( new EduBgCareerRapTableModel( ( Date) null, "賞罰"));
                for ( int i = 1; i <= rapCnt; i++) {
                    final String yearMonthKey = RAP_PREFIX.key() + "YearMonth" + i;
                    final String contKey = RAP_PREFIX.key() + "Content" + i;
                    EduBgCareerRapTableModel model = new EduBgCareerRapTableModel( baseParams.get( yearMonthKey), baseParams.get( contKey));
                    models.add( model);
                }
                models.add( new EduBgCareerRapTableModel( ( Date) null, "以上"));
                rapParams.addAll( models);
            }
            usedRowCnt -= rapCnt + 2;
            params.put( RAP_PREFIX, rapParams);
        }
        return params;
    }

    /**
     * 学歴とかのテーブルと、資格･免許テーブルに納まる量かどうか確認する
     * @return
     */
    public boolean validate() {
        boolean isValid = true;
        // 学歴とかの方
        {
            final int maxRows = 22 - 2 - 2; // 全行数 - 学歴ヘッダ・フッタ - 職歴ヘッダ・フッタ
            int eduBgCnt = Integer.valueOf( baseParams.get( EDU_BG_CNT.key())) + 2;
            int careerCnt = Integer.valueOf( baseParams.get( CAREER_CNT.key())) + 2;
            boolean isEnableRap = Boolean.valueOf( baseParams.get( IS_RAP_ENABLED.key()));
            int rapCnt = isEnableRap ? Integer.valueOf( baseParams.get( RAP_CNT.key())) + 2 : 0;
            isValid = isValid && (maxRows - eduBgCnt - careerCnt - rapCnt >= 0);
        }
        // 資格・免許
        {
            final int maxRows = 6;
            int licenseCnt = Integer.valueOf( baseParams.get( LICENSE_CNT.key()));
            isValid = isValid && (maxRows - licenseCnt >= 0);
        }
        return isValid;
    }

}
