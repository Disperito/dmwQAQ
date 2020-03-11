const axios = require('axios');

let pageSize = () => {
    let ps;
    return (ps = getQueryVariable('ps')) ? ps : 1;
};
let pageNum = () => {
    let pn;
    return (pn = getQueryVariable('pn')) ? pn : 1;
};

function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return (false);
}

axios.get('/article/get/' + pageSize + '/' + pageNum)
    .then(function (response) {
        const pageInfo = response.data;
        const articles = pageInfo.list;

        new Vue({
            el: ".article-list",
            data: articles
        })
    });