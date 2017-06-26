/**
 * 
 */
package com.knowshare.test.api.general;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author miguel
 *
 */
@Configuration
@ComponentScan(basePackages={"com.knowshare.api.controller"},lazyInit=true)
public class ConfigApiContext {

}
