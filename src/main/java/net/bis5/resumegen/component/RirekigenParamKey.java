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

/**
 * 履歴書生成パラメータのキー<br>
 * keyはクライアントからのリクエスト、列挙値はパース後のMapキーとして利用する想定
 * 
 * @author maruTA_bis5
 */
public enum RirekigenParamKey {

	/** 出力日 */
	REPORT_DATE("reportDate"),
	/** 氏名 */
	NAME("name"),
	/** 氏名カナ */
	NAME_KANA("nameKana"),
	/** 性別 */
	GENDER("gender"),
	/** 生年月日 */
	BIRTHDAY("birthday"),
	/** 出力日時点の満年齢 */
	AGE(null),
	/** 主住所 */
	MAIN_ADDRESS("mainAddress"),
	/** 主住所カナ */
	MAIN_ADDRESS_KANA("mainAddressKana"),
	/** 主住所郵便番号 */
	MAIN_ADDRESS_POSTAL("mainAddressPostal"),
	/** 副住所 */
	SUB_ADDRESS("subAddress"),
	/** 副住所カナ */
	SUB_ADDRESS_KANA("subAddressKana"),
	/** 副住所郵便番号 */
	SUB_ADDRESS_POSTAL("subAddressPostal"),
	/** 主電話番号 */
	MAIN_TEL("mainTel"),
	/** 副電話番号 */
	SUB_TEL("subTel"),
	/** 学歴 入力された数 */
	EDU_BG_CNT("eduBgCnt"),
	/** 職歴 入力された数 */
	CAREER_CNT("careerCnt"),
	/** 賞罰 入力された数 */
	RAP_CNT("rapCnt"),
	/** 学歴キープレフィックス */
	EDU_BG_PREFIX("eduBg"),
	/** 職歴キープレフィックス */
	CAREER_PREFIX("career"),
	/** 賞罰キープレフィックス */
	RAP_PREFIX("rap"),
	/** 賞罰出力有無 */
	IS_RAP_ENABLED("isRapEnable"),
	/** 資格・免許 入力された数 */
	LICENSE_CNT("licenseCnt"),
	/** 資格・免許キープレフィックス */
	LICENSE_PREFIX("license"),
	/** 志望の動機・特技・好きな学科・アピールポイントなど */
	PR("pr"),
	/** 通勤時間 */
	COMMUTING_TIME("commutingTime"),
	/** 扶養家族数（配偶者を除く） */
	NUM_FAMILY("numFamily"),
	/** 配偶者有無 */
	HAS_SPOUSE("hasSpouse"),
	/** 配偶者扶養義務の有無 */
	HAS_SPOUSE_DEPENDENT("hasSpouseDependent"),
	/** 本人希望記入欄 */
	DESIRE("desire");

	private RirekigenParamKey(String keyStr) {
		this.key = keyStr;
	}

	private String key;

	/**
	 * Get the key.
	 * 
	 * @return key
	 */
	public String key() {
		return key;
	}
}
