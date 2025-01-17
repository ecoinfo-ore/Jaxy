
package com.rac021.jaxy.ee.metrics ;

import com.rac021.jaxy.api.metrics.Metrics ;
import org.eclipse.microprofile.metrics.Metadata ;
import org.eclipse.microprofile.metrics.MetricType ;
import org.eclipse.microprofile.metrics.MetricUnits ;
import org.eclipse.microprofile.metrics.MetricRegistry ;

/**
 *
 * @author ryahiaoui
 */

public class MetricsManager {
    
    
    public static void registerMetric(   MetricRegistry metrics , String serviceCodeSnakeName ) {
        
            /** Rename ServiceCode from camelCase to Snake_case . */
            Metadata counterMetadata_XML = new Metadata( serviceCodeSnakeName + "_xml_plain_counter", 
                                                         MetricType.COUNTER ,
                                                         MetricUnits.NONE ) ;
            metrics.counter(counterMetadata_XML ) ;
            
            Metadata counterMetadata_XML_ENCRYPTED = new Metadata( serviceCodeSnakeName + "_xml_encrypted_counter",
                                                                   MetricType.COUNTER , 
                                                                   MetricUnits.NONE ) ;
            metrics.counter(counterMetadata_XML_ENCRYPTED) ;
            
            Metadata counterMetadata_JSON = new Metadata( serviceCodeSnakeName + "_json_plain_counter",
                                                          MetricType.COUNTER ,
                                                          MetricUnits.NONE ) ;
            metrics.counter(counterMetadata_JSON ) ;
            
            Metadata counterMetadata_JSON_ENCRYPTED = new Metadata( serviceCodeSnakeName + "_json_encrypted_counter",
                                                                    MetricType.COUNTER , 
                                                                    MetricUnits.NONE ) ;
            metrics.counter(counterMetadata_JSON_ENCRYPTED ) ;
            
            Metadata counterMetadata_TEMPLATE = new Metadata( serviceCodeSnakeName + "_template_plain_counter",
                                                              MetricType.COUNTER ,
                                                              MetricUnits.NONE ) ;
            metrics.counter(counterMetadata_TEMPLATE ) ;
            
            Metadata counterMetadata_TEMPLATE_ENCRYPTED = new Metadata ( serviceCodeSnakeName + "_template_encrypted_counter", 
                                                                         MetricType.COUNTER , 
                                                                         MetricUnits.NONE ) ;
            metrics.counter(counterMetadata_TEMPLATE_ENCRYPTED ) ;

            // TIMERS 
            
            Metadata timerMetadata_XML = new Metadata( serviceCodeSnakeName + "_xml_plain_timer", 
                                                       MetricType.TIMER,
                                                       MetricUnits.MILLISECONDS ) ;
            
            Metrics.addTimerService( serviceCodeSnakeName + "_xml_plain_timer",
                                     metrics.timer(timerMetadata_XML ) )      ; 

            Metadata timerMetadata_XML_ENCRYPTED = new Metadata( serviceCodeSnakeName + "_xml_encrypted_timer",
                                                                 MetricType.TIMER                             , 
                                                                 MetricUnits.MILLISECONDS ) ;

            Metrics.addTimerService( serviceCodeSnakeName + "_xml_encrypted_timer" , 
                                     metrics.timer(timerMetadata_XML_ENCRYPTED ) ) ; 
            
            Metadata timerMetadata_JSON = new Metadata( serviceCodeSnakeName + "_json_plain_timer",
                                                        MetricType.TIMER                          ,
                                                        MetricUnits.MILLISECONDS ) ;

            Metrics.addTimerService( serviceCodeSnakeName + "_json_plain_timer",
                                     metrics.timer(timerMetadata_JSON ) )      ; 

            Metadata timerMetadata_JSON_ENCRYPTED = new Metadata( serviceCodeSnakeName + "_json_encrypted_timer",
                                                                  MetricType.TIMER                              , 
                                                                  MetricUnits.MILLISECONDS ) ;
            
            Metrics.addTimerService(  serviceCodeSnakeName + "_json_encrypted_timer", 
                                      metrics.timer(timerMetadata_JSON_ENCRYPTED ) ) ; 
            
            Metadata timerMetadata_TEMPLATE = new Metadata( serviceCodeSnakeName + "_template_plain_timer",
                                                            MetricType.TIMER                              , 
                                                            MetricUnits.MILLISECONDS ) ;
            
            Metrics.addTimerService(  serviceCodeSnakeName + "_template_plain_timer", 
                                      metrics.timer(timerMetadata_TEMPLATE ) )      ; 

            Metadata timerMetadata_TEMPLATE_ENCRYPTED = new Metadata( serviceCodeSnakeName + "_template_encrypted_timer",
                                                                      MetricType.TIMER                                  , 
                                                                      MetricUnits.MILLISECONDS ) ;
            
            Metrics.addTimerService(  serviceCodeSnakeName + "_template_encrypted_timer" , 
                                      metrics.timer(timerMetadata_TEMPLATE_ENCRYPTED ) ) ; 
        
    }
    
}
