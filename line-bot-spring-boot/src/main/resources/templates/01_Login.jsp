<%@ page language="java" import = "javax.servlet.http.*" import = "java.servlet.*"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<!doctype html>
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <title>ログイン画面</title>
    </head>
    <body>
        <!-- コンテンツ -->
        <div class="login">
            <div class="loginContents">
                <form method="POST">
                    <fieldset class="form-group">
                        <div class="form-group">
                            <label class="font-weight-bold" >社員ID</label>
                            <br>
                            <input type="text" tabindex="10" class="form-control">
                        </div>
                        <div class="form-group">
                            <label class="font-weight-bold">パスワード</label>
                            <br>
                            <input type="password" tabindex="20" class="form-control">
                        </div>
                    </fieldset>
               </form>
           </div>
       </div>
    </body>
</html>