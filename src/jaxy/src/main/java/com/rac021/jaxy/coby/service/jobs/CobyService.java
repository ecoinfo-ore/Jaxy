
package com.rac021.jaxy.coby.service.jobs ;

/**
 *
 * @author ryahiaoui
 */

import javax.ws.rs.GET ;
import javax.ws.rs.Produces ;
import javax.ws.rs.HeaderParam ;
import javax.ws.rs.core.UriInfo ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.Response ;
import java.util.stream.Collectors ;
import javax.annotation.PostConstruct ;
import com.rac021.jaxy.coby.checker.TokenManager;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry;
import com.rac021.jaxy.api.qualifiers.security.Policy;
import com.rac021.jaxy.api.qualifiers.security.Secured;
import com.rac021.jaxy.api.exceptions.BusinessException;
import static com.rac021.jaxy.coby.scheduler.COBYScheduler.JOBS;

/**
 *
 * @author R.Yahiaoui
 */

@ServiceRegistry("coby_jobs")
@Secured(policy = Policy.CustomSignOn )
// @Cipher( cipherType = { CipherTypes.AES_128_CBC, CipherTypes.AES_256_CBC } )

public class CobyService    {
 
    @PostConstruct
    public void init() {
    }

    public CobyService() {
    }
    
    @GET
    @Produces( {  "xml/plain" } )
    public Response list ( @HeaderParam("API-key-Token") String token ,
                           @HeaderParam("keep") String filterdIndex   , 
                           @Context UriInfo uriInfo ) throws BusinessException {    
    
         String s         = " EMPTY JOB LIST \n "        ;
         
         String login     = TokenManager.getLogin(token) ;
           
         if( JOBS != null && ! JOBS.isEmpty()) {
            s= JOBS.stream()
                   .filter( job -> job != null)
                   .filter( job ->  login.equalsIgnoreCase("admin") || job.trim().startsWith(login) )
                   .map(Object::toString )
                   .collect(Collectors.joining(" \n -------------------------- \n ")) ;
         }
         return Response.status(Response.Status.OK)
                        .entity( "\n " + s )
                        .build() ;
    }
    
}

