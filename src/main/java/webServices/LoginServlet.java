package webServices;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import beans.ClientType;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CouponClientFacade;
import facade.CustomerFacade;
import singleton.CouponSystem;

/**
 * <h1>LoginServlet</h1> The LoginServlet class meant creates an
 * HttpServletRequest object and passes it as an argument to the
 * RestController's service methods (doGet, doPost, etc).
 */
@Controller
public class LoginServlet {
	Logger logger = LoggerService.getLogger();
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doPostRedirect(

			@RequestParam(value = "name") String userName, @RequestParam(value = "pwd") String password,
			@RequestParam(value = "usertype") ClientType type, HttpServletRequest request) {

		CouponClientFacade cf = (new CouponSystem()).Login(userName, password, type); 
		if (cf == null) {
			logger.warn(request+" user name: "+userName+" / password: "+password+" / client type: "+type+ ". incorrect, ip: " + request.getRemoteAddr());
			return "redirect:http://localhost:8080/login.html";
		}

		request.getSession().setAttribute("facade", cf);

		switch (type) {
		case admin:
			AdminFacade adminFacade = (AdminFacade) cf;
			request.getSession().setAttribute("adminFacade", adminFacade);
			return "redirect:http://localhost:8080/admin/index.html";
		case company:
			CompanyFacade companyFacade = (CompanyFacade) cf;
			request.getSession().setAttribute("companyFacade", companyFacade);
			return "redirect:http://localhost:8080/company/index.html";
		case customer:
			CustomerFacade customerFacade = (CustomerFacade) cf;
			request.getSession().setAttribute("customerFacade", customerFacade);
			return "redirect:http://localhost:8080/customer/index.html";
		}

		return "redirect:http://localhost:8080/login.html";
	}

}
