package com.ibm.grocery.api;

import com.ibm.grocery.domain.Roles;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/api")
@LoginConfig(authMethod = "MP-JWT")
@DeclareRoles({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
public class GroceryApplication extends Application {
}
