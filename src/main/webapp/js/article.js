const axios = require('axios');
/*$.ajax({
    url: '/article/get/' + getPathID(),
    type: 'get',
    success: function (response_text) {
        article = JSON.parse(response_text);

        let app = new Vue({
            el: ".article",
            data: {
                article: article
            },

        });
    }
});*/

axios.get('/article/get/'+getPathID())
    .then(function (response) {
        let app = new Vue({
            el: ".article",
            data: {
                article: JSON.parse(response.toString())
            },
        });
    });

function isLogined() {
    // TODO
    return false;
}

new Vue({
    el: "#nav-login-btn",
    computed: {
        nav_login_btn_info: function () {
            return isLogined() ? "" : "登录";
        }
    }
});

new Vue({
    el: "#recent-article-list",
    data: {
        articles: [
            {title: "震惊！一网站竟上传这种内容..!快来看！！", href: "#"}
            , {title: "震惊！一网站竟上传这种内容..!快来看！！", href: "#"}
            , {title: "震惊！一网站竟上传这种内容..!快来看！！", href: "#"}
            , {title: "震惊！一网站竟上传这种内容..!快来看！！", href: "https://www.baidu.com"}
            , {title: "震惊！一网站竟上传这种内容..!快来看！！", href: "#"}
        ]
    }
});


function getPathID() {
    const query = window.location.toString();
    return query.substring(query.lastIndexOf("/") + 1, query.length);
}