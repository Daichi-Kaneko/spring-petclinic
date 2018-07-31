package org.springframework.samples.petclinic.owner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	private final OwnerRepository owners;

	@Autowired
	private HttpSession session;

	public LoginController(OwnerRepository clinicService) {
		this.owners = clinicService;
	}

	@GetMapping("/login")
	public String Getlogin(Owner owner, Map<String, Object> model) {

		return "login";
	}

	@PostMapping("/login")
	public String Postlogin(@Valid Owner owner, BindingResult result, Map<String, Object> model, String algorithmName) {
//		Collection<Owner> results = this.owners.findvalue(owner.getEmail(),"");
//		Collection<Owner> password = this.owners.findvalue("",owner.getPassword());
//		Collection<Owner> results = this.owners.findLogin(owner.getEmail(), passwordEncoder.encode(owner.getPassword()));
		MessageDigest md = null;
		StringBuilder sb = null;
		algorithmName = "MD2";
		String password = owner.getPassword();
		try {
			md = MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(password.getBytes());
		sb = new StringBuilder();
		for (byte b : md.digest()) {
			String hex = String.format("%02x", b);
			sb.append(hex);
		}
		owner.setPassword(sb.toString());
		Collection<Owner> results = this.owners.findLogin(owner.getEmail(), owner.getPassword());
		if (results.isEmpty()) {
			// no email found
			result.rejectValue("email", "loginerr", "loginerr");
			// no pass found
			result.rejectValue("password", "loginerr", "loginerr");
			return "login";
		} else {
			session.setAttribute("firstname", owner.getFirstName());
			return "welcome";
		}

	}
}
