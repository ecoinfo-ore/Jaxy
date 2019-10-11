
package com.rac021.jaxy.coby.service.cancel ;

import javax.ws.rs.GET ;
import javax.ws.rs.Produces ;
import javax.ws.rs.HeaderParam ;
import javax.ws.rs.core.UriInfo ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.Response ;
import javax.annotation.PostConstruct ;
import com.rac021.jaxy.coby.checker.TokenManager;
import com.rac021.jaxy.coby.scheduler.COBYScheduler;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry;
import com.rac021.jaxy.api.qualifiers.security.Policy;
import com.rac021.jaxy.api.qualifiers.security.Secured;
import com.rac021.jaxy.api.exceptions.BusinessException;
import static com.rac021.jaxy.coby.scheduler.PipelineRunner.process;
import static com.rac021.jaxy.coby.scheduler.COBYScheduler.SUBMITTED_JOB;

/**
 *
 * @author R.Yahiaoui
 */

@ServiceRegistry("coby_cancel")
@Secured(policy = Policy.CustomSignOn )
// @Cipher( cipherType = { CipherTypes.AES_128_CBC, CipherTypes.AES_256_CBC } )

public class CobyService    {
   
    @PostConstruct
    public void init() {
    }

    public CobyService() {
    }
    
    @GET
    @Produces( {  "xml/plain"  } )
    public Response cancel ( @HeaderParam("API-key-Token") String token ,
                             @HeaderParam("keep") String filterdIndex   , 
                             @Context UriInfo uriInfo ) throws BusinessException     {    
         
        //executorService.shutdownNow() ;
        
        if( COBYScheduler.jobOwner == null ) {
            
           return Response.status(Response.Status.OK)
                          .entity("\n No Job submited to Cancel \n" )
                          .build() ;
        }
        
        String login     = TokenManager.getLogin(token) ;
        
        if( COBYScheduler.jobOwner.equals(login )) {
        
            if( SUBMITTED_JOB != null )   {

               SUBMITTED_JOB.cancel(true) ;
               process.destroyForcibly()  ;
               return Response.status(Response.Status.OK)
                              .entity("\n Current Job Canceled .. \n" )
                              .build() ;
            } else {
                
                return Response.status(Response.Status.OK)
                          .entity("\n No Job submited to Cancel \n" )
                          .build() ;
            }
        }
        else {
            
            return Response.status(Response.Status.OK)
                           .entity("\n [" + login + "] does not have permissions \n"
                                   + "  -> Current JobOwner : " + COBYScheduler.jobOwner )
                           .build() ;
        }
    }
}
