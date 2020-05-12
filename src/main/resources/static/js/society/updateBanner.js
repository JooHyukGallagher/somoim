const bannerImage = {
    init: function () {
        $(function() {
            cropper = '';
            let $confirmBtn = $("#confirm-button");
            let $resetBtn = $("#reset-button");
            let $cutBtn = $("#cut-button");
            let $saveBtn = $("#save-button");
            let $newSocietyImage = $("#new-society-image");
            let $currentSocietyImage = $("#current-society-image");
            let $resultImage = $("#cropped-new-society-image");
            let $societyImage = $("#societyImage");

            $newSocietyImage.hide();
            $cutBtn.hide();
            $resetBtn.hide();
            $confirmBtn.hide();
            $saveBtn.hide();

            $("#society-image-file").change(function(e) {
                if (e.target.files.length === 1) {
                    const reader = new FileReader();
                    reader.onload = e => {
                        if (e.target.result) {
                            if (!e.target.result.startsWith("data:image")) {
                                alert("이미지 파일을 선택하세요.");
                                return;
                            }

                            let img = document.createElement("img");
                            img.id = 'new-society';
                            img.src = e.target.result;
                            img.setAttribute('width', '100%');

                            $newSocietyImage.html(img);
                            $newSocietyImage.show();
                            $currentSocietyImage.hide();

                            let $newImage = $(img);
                            $newImage.cropper({aspectRatio: 13/2});
                            cropper = $newImage.data('cropper');

                            $cutBtn.show();
                            $confirmBtn.hide();
                            $resetBtn.show();
                        }
                    };

                    reader.readAsDataURL(e.target.files[0]);
                }
            });

            $resetBtn.click(function() {
                $currentSocietyImage.show();
                $newSocietyImage.hide();
                $resultImage.hide();
                $resetBtn.hide();
                $cutBtn.hide();
                $confirmBtn.hide();
                $saveBtn.hide();
                $societyImage.val('');
            });

            $cutBtn.click(function () {
                let dataUrl = cropper.getCroppedCanvas().toDataURL();

                if (dataUrl.length > 1000 * 1024) {
                    alert("이미지 파일이 너무 큽니다. 1024000 보다 작은 파일을 사용하세요. 현재 이미지 사이즈 " + dataUrl.length);
                    return;
                }

                let newImage = document.createElement("img");
                newImage.id = "cropped-new-society-image";
                newImage.src = dataUrl;
                newImage.width = 640;
                $resultImage.html(newImage);
                $resultImage.show();
                $confirmBtn.show();

                $confirmBtn.click(function () {
                    $newSocietyImage.html(newImage);
                    $cutBtn.hide();
                    $confirmBtn.hide();
                    $societyImage.val(dataUrl);
                    $saveBtn.show();
                });
            });

            $saveBtn.click(function() {
                $("#imageForm").submit();
            })
        });
    }
};

document.addEventListener("DOMContentLoaded", () => {
    bannerImage.init();
});