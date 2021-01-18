// 입력 값들이 유효성 겁사가 진행되어있는지 확인하기 위한 객체 생성
var validateCheck = {
    "id": false,
    "pwd1": false,
    "pwd2": false,
    "name": false,
    "phone": false,
    "nickName": false,
    "email": false,
}

// 아이디 유효성 검사
// 첫 글자는 영어 소문자, 나머지 글자는 영어 대, 소문자 + 숫자, 총 6~12글자
$("#id").on("input", function(){
    var regExp = /^[a-z][a-zA-z\d]{5,11}$/;

    var value = $("#id").val();
    if(!regExp.test(value)){
        $("#checkId").text("아이디 형식이 유효하지 않습니다.").css("color", "red");
        validateCheck.id = false;
    }else{
        // $("#checkId").text("유효한 아이디 형식입니다.").css("color", "green");
        $.ajax({
            url: "signUp.do",
            data: {"id": value},
            type: "post",
            success: function(idDup){
                if(idDup == 0){ // 중복되지 않는 경우
                    $("#chekId").text("사용 가능한 아이디입니다.").css("color", "green");
                    validateCheck.id = true;
                }else{
                    $("#checkId").text("이미 사용중인 아이디입니다.").css("color", "red");
                    validateCheck.id = false;
                }
            },
            error: function(){
                console.log("아이디 중복검사 실패");
            }
        });
    }
});

// 이름 유효성 검사
// 한글 두 글자 이상
$("#name").on("input", function(){
    var regExp = /^[가-힇]{2,}$/;

    var value = $("#name").val();
    if(!regExp.test(value)){
        $("#checkName").text("이름 형식이 유효하지 않습니다.").css("color", "red");
        validateCheck.name = false;
    }else{
        $("#checkName").text("유효한 이름 형식입니다.").css("color", "green");
        validateCheck.name = true;
    }
});

// 이메일 유효성 검사
$("#email").on("input", function(){
    var regExp = /^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/;

    var value = $("#email").val();
    if(!regExp.test(value)){
        $("#checkEmail").text("이메일 형식이 유효하지 않습니다.").css("color", "red");
        validateCheck.email = false;
    }else{
        $("#checkEmail").text("유효한 이메일 형식입니다.").css("color", "green");
        validateCheck.email = true;
    }
});

// 비밀번호 유효성 검사
// 영어 대, 소문자 + 숫자, 총 6~12글자
$("#pwd1, #pwd2").on("input", function(){
    var regExp = /^[a-zA-z\d]{6,12}$/;

    var v1 = $("#pwd1").val();
    var v2 = $("#pwd2").val();

    if(!regExp.test(v1)){
        $("#checkPwd1").text("비밀번호 형식이 유효하지 않습니다.").css("color", "red");
        validateCheck.pwd1 = false;
    }else{
        $("#checkPwd1").text("유효한 비밀번호 형식입니다.").css("color", "green");
        validateCheck.pwd1 = true;
    }

    // 비밀번호가 유효하지 않은 상태에서
    // 비밀번호 확인 작성 시
    if(!validateCheck.pwd1 && v2.length > 0){
        // 유효성 검사를 하지 않은 상태에서 비번 확인의 길이가 0보다 긴 경우
        swal("유효한 비밀번호를 먼저 작성해주세요.");
        $("#pwd2").val(""); // 비밀번호 확인에 입력한 값 삭제
        $("#pwd1").focus();
    }else{
        // 비밀번호, 비밀번호 확인 일치 여부
        if(v1.length == 0 || v2.length == 0){
            $("#checkPwd2").html("&nbsp;");
        }else if(v1 == v2){
            $("#checkPwd2").text("비밀번호 일치").css("color", "green");
            validateCheck.pwd2 = true;
        }else{
            $("#checkPwd2").text("비밀번호 불일치").css("color", "red");
            validateCheck.pwd2 = false;
        }
    }
});

// 전화번호 유효성 검사
$("#phone").on("input", function(){
    // input 태그에 11글자 초과 입력하지 못하게
    if($(this).val().length > 11){
        $(this).val($(this).val().slice(0,11));
    }

    var regExp = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;

    var v1 = $("#phone").val();

    if(!regExp.test(v1)){
        $("#checkPhone").text("전화번호가 유효하지 않습니다.").css("color", "red");
        validateCheck.phone = false;
    }else{
        $("#checkPhone").text("유효한 형식의 전화번호입니다.").css("color", "green");
        validateCheck.phone = true;
    }
});

// 닉네임 유효성 검사
$("#nickName").on("input", function(){
    var regExp = /^[\w\Wㄱ-ㅎㅏ-ㅣ가-힣]{2,20}$/;

    var value = $("#nickName").val();
    if(!regExp.test(value)){
        $("#checknickName").text("닉네임 형식이 유효하지 않습니다.").css("color", "red");
        validateCheck.nickName = false;
    }else{
        $("#checknickName").text("닉네임 이름 형식입니다.").css("color", "green");
        validateCheck.nickName = true;
    }
});


// 비밀번호 수정
function pwdValidate(){
    var regExp = /^[a-zA-z\d]{6,12}$/;

    if(!regExp.test($("#newPwd1").val())){
        swal("비밀번호 형식이 유효하지 않습니다.");
        $("#newPwd1").focus();

        return false;
    }

    // 새로운 비밀번호와 비밀번호 확인이 일치하지 않을 때
    if($("#newPwd1").val() != $("#newPwd2").val()){
        swal("새로운 비밀번호가 일치하지 않습니다.");

        $("#newPwd1").focus();
        $("#newPwd1").val("");
        $("#newPwd2").val("");

        return false;
    }
}


function validate(){
    // 유효성 검사 여부 확인작업
    for(var key in validateCheck){
        if(!validateCheck[key]){
            // 어떤 key값 중 하나라도 false가 나왔을 때

            var msg;
            switch(key){
                case "id": msg="아이디가"; break;
                case "pwd1": 
                case "pwd2": msg="비밀번호가"; break;
                case "name": msg="이름이"; break;
                case "phone": msg="전화번호가"; break;
                case "email": msg="이메일이"; break;
                case "nickName": msg="닉네임이"; break;
            }
            swal(msg + " 유효하지 않습니다.");

            $("#" + key).focus();

            return false;
        }
    }
}


// 회원 정보 수정 
function memberUpdateValidate(){
    // 각 유효성 검사를 저장할 객체
    var updateCheck = { "name": false,
                        "phone": false,
                        "nickName": false,
                        "email": false}

    // 이름 유효성 검사
    var regExp1 = /^[가-힇]{2,10}$/;
    if(!regExp1.test($("#name").val())){
        updateCheck.name = false;
    }else{
        updateCheck.name = true;
    }

    // updateCheck 내부에 저장된 값 검사
    for(var key in updateCheck){
        if(!updateCheck[key]){ // false인 경우
            swal("일부 값이 유효하지 않습니다.");
            return false;
        }
    }
}