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

import static net.bis5.resumegen.component.RirekigenParamKey.CAREER_PREFIX;
import static net.bis5.resumegen.component.RirekigenParamKey.EDU_BG_PREFIX;
import static net.bis5.resumegen.component.RirekigenParamKey.LICENSE_PREFIX;
import static net.bis5.resumegen.component.RirekigenParamKey.RAP_PREFIX;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.bis5.resumegen.entity.EduBgCareerRapTableModel;

import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.reports.exporter.ExcelExporter;
import org.bbreak.excella.reports.listener.ReportProcessAdaptor;
import org.bbreak.excella.reports.listener.ReportProcessListener;
import org.bbreak.excella.reports.model.ReportBook;
import org.bbreak.excella.reports.model.ReportSheet;
import org.bbreak.excella.reports.processor.ReportProcessor;
import org.bbreak.excella.reports.tag.SingleParamParser;

/**
 * @author maruTA_bis5
 */
public class ReportGenerator {

    private final Map<RirekigenParamKey, Object> params;

    private final Map<RirekigenParamKey, List<Consumer<Object>>> paramProcessors = new LinkedHashMap<RirekigenParamKey, List<Consumer<Object>>>();

    private final List<BiConsumer<Map<RirekigenParamKey, Object>, Map<String, Object>>> preProcessors = new LinkedList<BiConsumer<Map<RirekigenParamKey, Object>, Map<String, Object>>>();

    private final List<Consumer<ReportSheet>> postProcessors = new LinkedList<Consumer<ReportSheet>>();

    private final List<ReportProcessListener> reportProcListeners = new LinkedList<ReportProcessListener>();

    public ReportGenerator( Map<RirekigenParamKey, Object> params) {
        this.params = params;
        initParamProcessors();
        initReportProcListeners();
    }

    /**
	 * 
	 */
    private void initParamProcessors() {
        addPreProcessor( new BiConsumer<Map<RirekigenParamKey, Object>, Map<String, Object>>() {
            @Override
            public void accept( Map<RirekigenParamKey, Object> t, Map<String, Object> u) {
                int cnt = 1;
                // 学歴
                {
                    @SuppressWarnings( "unchecked")
                    List<EduBgCareerRapTableModel> models = ( List<EduBgCareerRapTableModel>) t.get( EDU_BG_PREFIX);
                    for ( EduBgCareerRapTableModel model : models) {
                        setTagParam( model, cnt, u);
                        cnt++;
                    }
                    t.remove( EDU_BG_PREFIX);
                }
                // 職歴
                {
                    @SuppressWarnings( "unchecked")
                    List<EduBgCareerRapTableModel> models = ( List<EduBgCareerRapTableModel>) t.get( CAREER_PREFIX);
                    for ( EduBgCareerRapTableModel model : models) {
                        setTagParam( model, cnt, u);
                        cnt++;
                    }
                    t.remove( CAREER_PREFIX);
                }
                // 賞罰
                {
                    @SuppressWarnings( "unchecked")
                    List<EduBgCareerRapTableModel> models = ( List<EduBgCareerRapTableModel>) t.get( RAP_PREFIX);
                    if ( models != null) {
                        for ( EduBgCareerRapTableModel model : models) {
                            setTagParam( model, cnt, u);
                            cnt++;
                        }
                    }
                    t.remove( RAP_PREFIX);
                }
            }

            private void setTagParam( EduBgCareerRapTableModel model, int cnt, Map<String, Object> u) {
                final String yearTag = "year" + cnt;
                final String monthTag = "month" + cnt;
                final String contTag = "cont" + cnt;
                u.put( yearTag, model.getYear());
                u.put( monthTag, model.getMonth());
                u.put( contTag, model.getContent());
            }
        });

        addPreProcessor( new BiConsumer<Map<RirekigenParamKey, Object>, Map<String, Object>>() {
            @Override
            public void accept( Map<RirekigenParamKey, Object> t, Map<String, Object> u) {
                // 資格・免許
                int cnt = 1;
                @SuppressWarnings( "unchecked")
                List<EduBgCareerRapTableModel> models = ( List<EduBgCareerRapTableModel>) t.get( LICENSE_PREFIX);
                for ( EduBgCareerRapTableModel model : models) {
                    final String yearTag = "lYear" + cnt;
                    final String monthTag = "lMonth" + cnt;
                    final String contTag = "lCont" + cnt;
                    u.put( yearTag, model.getYear());
                    u.put( monthTag, model.getMonth());
                    u.put( contTag, model.getContent());
                    cnt++;
                }
            }
        });
    }

    private void initReportProcListeners() {
        reportProcListeners.add( new ReportProcessAdaptor() {
            /*
             * (non Javadoc)
             * @see org.bbreak.excella.reports.listener.ReportProcessAdaptor#
             * postBookParse(org.apache.poi.ss.usermodel.Workbook,
             * org.bbreak.excella.reports.model.ReportBook)
             */
            @Override
            public void postBookParse( Workbook workbook, ReportBook reportBook) {
                workbook.setActiveSheet( 2);
            }
        });
    }

    public void addPreProcessor( BiConsumer<Map<RirekigenParamKey, Object>, Map<String, Object>> proc) {
        preProcessors.add( proc);
    }

    public void addPostProcessor( Consumer<ReportSheet> proc) {
        postProcessors.add( proc);
    }

    public void addReportProcessListener( ReportProcessListener listener) {
        reportProcListeners.add( listener);
    }

    public boolean doProcess( String outputPath, String templatePath) {
        ReportBook reportBook = new ReportBook( templatePath, outputPath, ExcelExporter.FORMAT_TYPE);
        ReportSheet leftSheet = new ReportSheet( "左");
        ReportSheet rightSheet = new ReportSheet( "右");
        ReportSheet reportSheet = new ReportSheet( "A3");

        Map<String, Object> excellaParamMap = new HashMap<String, Object>();
        preProcess( excellaParamMap, params);
        for ( Entry<String, Object> param : excellaParamMap.entrySet()) {
            reportSheet.addParam( SingleParamParser.DEFAULT_TAG, param.getKey(), param.getValue());
        }
        postProcess( reportSheet);

        reportBook.addReportSheets( Arrays.asList( leftSheet, rightSheet, reportSheet));
        ReportProcessor processor = new ReportProcessor();
        registerReportProcListener( processor);
        try {
            processor.process( reportBook);
            return true;
        }
        catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void preProcess( Map<String, Object> exParams, Map<RirekigenParamKey, Object> params) {
        for ( BiConsumer<Map<RirekigenParamKey, Object>, Map<String, Object>> proc : preProcessors) {
            proc.accept( params, exParams);
        }
    }

    private void postProcess( ReportSheet reportSheet) {
        for ( Consumer<ReportSheet> proc : postProcessors) {
            proc.accept( reportSheet);
        }
    }

    private void registerReportProcListener( ReportProcessor processor) {
        for ( ReportProcessListener listener : reportProcListeners) {
            processor.addReportProcessListener( listener);
        }
    }
}
