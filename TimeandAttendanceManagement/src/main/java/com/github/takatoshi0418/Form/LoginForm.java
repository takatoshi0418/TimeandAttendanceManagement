package com.github.takatoshi0418.Form;

import lombok.Data;

/**
 * ログインフォームクラス
 * ログイン画面から送信されるデータを受け取るためのクラス
 */
@Data
public class LoginForm {

    /** 社員場  */
    private String employeeNumber;

    /** パスワード  */
    private String password;
}
