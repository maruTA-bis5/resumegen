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
package net.bis5.resumegen;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import net.arnx.jsonic.JSON;
import net.bis5.resumegen.component.ParamParser;
import net.bis5.resumegen.component.ReportGenerator;
import net.bis5.resumegen.component.RirekigenParamKey;

/**
 * @author maruTA_bis5
 */
public class RirekigenCore {

    /**
     * 履歴書生成のパラメータ
     */
    private Map<String, String> baseParams;

    private ServletContext servletCxt;

    /**
     * @param baseParams
     */
    public RirekigenCore( Map<String, String> baseParams) {
        if ( baseParams == null || baseParams.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.baseParams = baseParams;
    }

    public RirekigenCore( Map<String, String> baseParams, ServletContext cxt) {
        this( baseParams);
        this.servletCxt = cxt;
    }

    /**
     * 履歴書作るよ
     * @return とりあえずJSONでIdentifierを返す
     */
    public String doProcess() {
        ParamParser parser = new ParamParser( baseParams);
        Map<RirekigenParamKey, Object> parsedParams = parser.parse();
        ReportGenerator reportGen = new ReportGenerator( parsedParams);
        String token = generateToken();
        reportGen.doProcess( getOutputFilePath( getTemporaryPath(), getFileNameByToken( token, false)),
            servletCxt.getRealPath( "/WEB-INF/Template.xls"));

        // XXX ちゃんと生成できていようがいまいが識別トークンを返すっていうクソ設計
        Map<String, String> returnKeyValue = new HashMap<String, String>();
        returnKeyValue.put( "token", token);
        return JSON.encode( returnKeyValue);
        // XXX JSONで帰ると思った?残念只の文字列でした！
        // return "\"" + token + "\"";
    }

    public InputStream getInputStream() {
        if ( baseParams.containsKey( "token")) {
            Path path = Paths.get( getTemporaryPath(), getFileNameByToken( ( String) baseParams.get( "token"), true));
            if ( Files.isRegularFile( path)) {
                try {
                    return Files.newInputStream( path);
                }
                catch ( IOException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private String getTemporaryPath() {
        Path path = Paths.get( System.getProperty( "java.io.tmpdir"));
        return path.toString();
    }

    private String getFileNameByToken( String token, boolean withExtension) {
        Path path = Paths.get( token);
        return path.getFileName() + (withExtension ? ".xls" : ""); // とりあえずxlsだけ対応しておけばいいだろう
    }

    private String getOutputFilePath( String tmpdir, String filename) {
        Path path = Paths.get( tmpdir, filename);
        return path.toString();
    }

    private String generateToken() {
        return String.valueOf( new Date().getTime()); // とりあえず。
    }

    /**
     * @return
     */
    public boolean isValidateRequest() {
        return baseParams.containsKey( "getRequestType") && "validate".equals( baseParams.get( "getRequestType"));
    }

    public String getValidateResult() {
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        ParamParser parser = new ParamParser( baseParams);
        result.put( "isValid", parser.validate());
        return JSON.encode( result);
    }
}
