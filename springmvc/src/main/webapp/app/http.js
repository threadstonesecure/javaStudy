var http = {}
http.ajax = function (button, url, data) {
    if (button) {
        button.prop("disabled", true);
    }

    var deferred = $.Deferred();
    $.ajax(url, {
        type: "post",
        dataType: "json",
        data: data
    }).done(function (json) {
            if (json.code !== 0) {
                showError(json.message || "操作发生错误");
                deferred.reject();
            } else {
                deferred.resolve(json);
            }
        }
    ).fail(function () {
        showError("服务器错误，请稍后再试");
        deferred.reject();
    }).always(function () {
        if (button) {
            button.prop("disabled", false);
        }
    });
    return deferred.promise();
};

// 调用
http.ajax("do/example", getFormData()).done(function (json) {
    // json.code === 0 总是成立
    // 正常处理 json.data 就好
});