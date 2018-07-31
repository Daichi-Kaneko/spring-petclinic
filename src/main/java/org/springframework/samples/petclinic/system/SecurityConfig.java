//package org.springframework.samples.petclinic.system;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import SampleAuthenticationFailureHandler.SampleAuthenticationFailureHandler;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/images/**", "/css/**", "/javascript/**", "/webjars/**");
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// 認可の設定
//		http.authorizeRequests().antMatchers("/*", "/owners/*","/owners/*/*").permitAll(); // indexは全ユーザーアクセス許可
//			//	.anyRequest().authenticated(); // それ以外は全て認証無しの場合アクセス不許可
//
//		// Login
//		http.formLogin().loginProcessingUrl("/login") // 認証処理のパス
//				.loginPage("/login") // ログインフォームのパス
//				.failureHandler(new SampleAuthenticationFailureHandler()) // 認証失敗時に呼ばれるハンドラクラス
//				.defaultSuccessUrl("/welcome") // 認証成功時の遷移先
//				.usernameParameter("email").passwordParameter("password") // ユーザー名、パスワードのパラメータ名
//				.and();
//
//		//LOGOUT設定
//		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout**")) // ログアウト処理のパス
//				.logoutSuccessUrl("/welcome"); // ログアウト完了時のパス
//
//	}

//	@Configuration
//	protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
//		@Autowired
//		UserDetailsServiceImpl userDetailsService;
//
//		@Override
//		public void init(AuthenticationManagerBuilder auth) throws Exception {
//			// 認証ユーザーを設定する
//			auth.userDetailsService(userDetailsService)
//					//bcryptでハッシュ化した値でパスワード認証を行う
//					.passwordEncoder(new BCryptPasswordEncoder());
//
//		}
//	}
	
//}

