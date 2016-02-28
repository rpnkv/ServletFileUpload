function FileUploadController($scope, fileUploadService){

    $scope.initFileUpload = initFileUpload;

    $scope.progress = 0;

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
        updateProgress(indexes.start,file.size);
        fileUploadService.uploadChunk(indexes,file).then(function(response){
            if(response.data.uploadStatus === "uploading"){
                uploadChunk({
                    start: response.data.nextChunkStartIndex,
                    end:response.data.nextChunkEndIndex
                },$scope.myFile);
            }else{
                if(response.data.uploadStatus === "uploaded"){
                    $scope.progress = 100;
                    alert("We've uploaded the file!")
                }
            }
        },function(){
            alert("Fatal upload error")
        })
    }

    function updateProgress(uploaded,total){
        if (uploaded === 0){
            $scope.progress = 0;
        }else{
            var onePercent = total/100;
            $scope.progress = Math.round(uploaded / onePercent);
        }
    }

}