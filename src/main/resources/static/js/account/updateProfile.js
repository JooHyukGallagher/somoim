const profileImage = {
    init: function () {
        cropper = '';
        let $confirmBtn = $('#confirm-button');                     // 확인 버튼
        let $resetBtn = $('#reset-button');                         // 취소 버튼
        let $cutBtn = $('#cut-button');                             // 자르기 버튼
        let $newProfileImage = $('#new-profile-image');             // 새로 띄운 이미지
        let $currentProfileImage = $('#current-profile-image');     // 현재 이미지
        let $resultImage = $('#cropped-new-profile-image');         // 자른 이미지
        let $profileImage = $('#profileImage');                     // profile 폼에 넣어줘야 하는 이미지

        // 초기 버튼과 새로운 프로필 이미지 숨기기
        $newProfileImage.hide();
        $cutBtn.hide();
        $resetBtn.hide();
        $confirmBtn.hide();

        // 처음 이미지 파일 불러올 때
        $('#profile-image-file').change(function (e) {          // 이미자 파일을 선택
            if (e.target.files.length === 1) {                  // 이미지 파일이 1개라면
                const reader = new FileReader();
                reader.onload = e => {                      // 이미지 파일을 읽어왔다면
                    if (e.target.result) {
                        // 이미지 파일인지 검증
                        if (!e.target.result.startsWith("data:image")) {
                            alert("이미지 파일을 선택하세요.");
                            return;
                        }

                        // 이미지 태그에 이미지 채워넣기
                        let img = document.createElement("img");
                        img.id = 'new-profile';
                        img.src = e.target.result;
                        img.setAttribute('width', '100%');

                        $newProfileImage.html(img);     // new-profile-image에 이미지태그를 추가
                        $newProfileImage.show();        // 새로 가져온 이미지를 보여줌
                        $currentProfileImage.hide();    // 처음 이미지는 숨김

                        // cropper 적용
                        let $newImage = $(img);
                        $newImage.cropper({aspectRatio: 1});
                        cropper = $newImage.data('cropper');

                        $cutBtn.show();         // 잘라내기 버튼 보이기
                        $confirmBtn.hide();     // 확인 버튼 숨기기
                        $resetBtn.show();       // 리셋 버튼 보이기
                    }
                };
                reader.readAsDataURL(e.target.files[0]);    // 이미지 파일을 읽어옴
            }
        });

        // 리셋 버튼을 눌렀을 때
        $resetBtn.click(function () {
            $currentProfileImage.show();        // 초기 이미지 보이기
            $newProfileImage.hide();            // 띄운 이미지 숨기기
            $resultImage.hide();                // 자른 이미지 숨기기
            $resetBtn.hide();                   // 리셋 버튼 숨기기
            $cutBtn.hide();                     // 자르기 버튼 숨기기
            $confirmBtn.hide();                 // 확인 버튼 숨기기
            $profileImage.val('');              // 프로필 폼에 들어갈 값 없애기
        });

        // 잘라내기 버튼을 눌렀을 때
        $cutBtn.click(function () {
            // data-url을 불러오기
            let dataUrl = cropper.getCroppedCanvas().toDataURL();

            // data-url의 크기 검증
            if (dataUrl.length > 1000 * 1024) {
                alert("이미지 파일이 너무 큽니다. 1024000보다 작은 파일을 사용하세요. 현재 이미지 사이즈 : " + dataUrl.length);
                return;
            }

            // 자른 이미지 넣기
            let newImage = document.createElement("img");
            newImage.id = "cropped-new-profile-image";
            newImage.src = dataUrl;
            newImage.width = 125;
            $resultImage.html(newImage);
            $resultImage.show();
            $confirmBtn.show();

            $confirmBtn.click(function () {
                $newProfileImage.html(newImage);
                $cutBtn.hide();
                $confirmBtn.hide();
                $profileImage.val(dataUrl);
            });
        });
    }
};

document.addEventListener("DOMContentLoaded", () => {
    profileImage.init();
});