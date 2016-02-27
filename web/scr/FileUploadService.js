function FileUploadService($http) {
    this.uploadFileToUrl = function (file, uploadUrl) {
        var fd = new FormData();
        fd.append('file', file.slice(0, 100));
        fd.append('param', "bla bla bla");
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function () {
            })
            .error(function () {
            });
    };

    this.initFileUpload = function (file) {
        var fd = new FormData();
        fd.append('username', "repnikov");
        fd.append('fileName', file.name);
        fd.append('fileModifyDate', file.lastModified);
        fd.append("fileSize", file.size);

        return $http.post("/upload/init", fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
    };

    this.uploadChunk = function(indexes,file){
        var fd = new FormData();
        fd.append('username', "repnikov");
        fd.append('fileName', file.name);
        fd.append('fileModifyDate', file.lastModified);
        fd.append("fileSize", file.size);

        fd.append('file',file.slice(indexes.start,indexes.end));

        return $http.post("/upload/chunk", fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
    }
}