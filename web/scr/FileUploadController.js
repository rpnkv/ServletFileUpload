function FileUploadController($scope, fileUploadService){

    $scope.initFileUpload = initFileUpload;

    function initFileUpload(){
        fileUploadService.initFileUpload($scope.myFile).then(function(response){
            if(response.data.uploadStatus === "uploading"){
                uploadChunk({
                    start: response.data.nextChunkStartIndex,
                    end:response.data.nextChunkEndIndex
                },$scope.myFile);
            }
        },function(){
            alert("Fatal upload error");
        })
    }

    function uploadChunk(indexes,file){
        fileUploadService.uploadChunk(indexes,file).then(function(response){
            if(response.data.uploadStatus === "uploading"){
                uploadChunk({
                    start: response.data.nextChunkStartIndex,
                    end:response.data.nextChunkEndIndex
                },$scope.myFile);
            }
        },function(){
            alert("Fatal upload error")
        })
    }

}