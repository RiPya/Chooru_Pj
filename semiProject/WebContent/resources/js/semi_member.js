// 입력 값들이 유효성 겁사가 진행되어있는지 확인하기 위한 객체 생성
var validateCheck = {
    "id": false,
    "pwd1": false,
    "pwd2": false,
    "name": false,
    "phone": false,
    "nickName": false,
    "email": false,
	"certify": false
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
            url: "idDupCheck.do",
            data: {"id": value},
            type: "post",
            success: function(result){
                if(result == 0){ // 중복되지 않는 경우
                    $("#checkId").text("사용 가능한 아이디입니다.").css("color", "green");
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
        // $("#checkEmail").text("유효한 이메일 형식입니다.").css("color", "green");
        $.ajax({
			url:"emailDupCheck.do",
			data: {"email" : value},
			type: "post",
			success: function(result){
				if(result == 0){
					$("#checkEmail").text("사용 가능한 이메일입니다.").css("color", "green");
					validateCheck.email = true;
				}else{
					$("#checkEmail").text("이미 사용중인 이메일입니다.").css("color", "red");
					validateCheck.email = false;
				}
			},
			error:function(){
				console.log("이메일 중복검사 실패");
			}
		});
	}
});

var sendKey;

// 이메일 인증을 위한 검사
$("#certifyBtn").on("click", function(){
	var email = $("#email").val();//사용자 이메일
	console.log(email)
	console.log( getContextPath())
	
	if(email == "" || !validateCheck.email){//이메일이 입력 안 됐거나 유효성 검사에 실패한 경우
		 swal({icon: "warning", title: "올바른 이메일을 입력하세요."});
	} else {
		 $.ajax({
				type : 'post',
				url : getContextPath() + '/sendEmail.do',
				data : {"email": email},
				async : false,
				success : function(result){
					$("#checkCertify").text("인증번호가 발송되었습니다.").css("color", "red");
					validateCheck.certify = false;
					console.log(result);
					
					sendKey = result;
				},
				error : function(){
					console.log("인증번호 발송 과정에서 오류 발생");
				}
		});//이메일 전송 ajax 끝
	}
});


function getContextPath() {
   var hostIndex = location.href.indexOf( location.host ) + location.host.length;
   return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
};


// 인증 키 일치여부 확인
$("#certify").on("propertychange change keyup paste input", function() {
		if ($("#certify").val() == sendKey) {   //인증 키 값을 비교를 위해 텍스트인풋과 벨류를 비교
			$("#checkCertify").text("인증 성공!").css("color", "green");
			validateCheck.certify = true;  //인증 성공여부 check
		} else {
			$("#checkCertify").text("인증번호 불일치!").css("color", "red");
			validateCheck.certify = false; //인증 실패
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
        $("#checkNickName").text("닉네임 형식이 유효하지 않습니다.").css("color", "red");
        validateCheck.nickName = false;
    }else{
        // $("#checkNickName").text("닉네임 이름 형식입니다.").css("color", "green");
		$.ajax({
			url: "nickNameDupCheck.do",
			data: {"nickName": value},
			type: "post",
			success: function(result){
				if(result == 0){
					$("#checkNickName").text("사용 가능한 닉네임입니다.").css("color", "green");
			        validateCheck.nickName = true;
				}else{
					$("#checkNickName").text("이미 사용중인 닉네임입니다.").css("color", "red");
					validateCheck.nickName = false;
				}
			},
			error: function(){
				console.log("닉네임 중복 검사 실패");
			}
		})
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
			var msf;
            switch(key){
                case "id": msg="아이디가"; break;
                case "pwd1": 
                case "pwd2": msg="비밀번호가"; break;
                case "name": msg="이름이"; break;
                case "phone": msg="전화번호가"; break;
                case "email": msg="이메일이"; break;
                case "nickName": msg="닉네임이"; break;
            }
			if(key == certify){
				swal({icon: "warning",
					  title: "이메일 인증이 완료되지 않았습니다."});
			}else{
	            swal(msg + " 유효하지 않습니다.");
			}
	        $("#" + key).focus();

            return false;
        }
    }
}


// 회원 정보 수정 
function updateMyInfoValidate(){
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


    // 전화번호 유효성 검사
    var regExp2 = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
    var p1 = $("#phone").val();

    if(!regExp2.test(p1)){
        updateCheck.phone = false;
    }else{
        updateCheck.phone = true;
    }
    
    // 이메일 유효성 검사
    var regExp4 =  /^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/;
    if(!regExp4.test($("#email").val())){
        updateCheck.email = false;
    }else{
        updateCheck.email = true;
    }
    
		
    // 닉네임 유효성 검사
    var regExp3 = /^[\w\Wㄱ-ㅎㅏ-ㅣ가-힣]{2,20}$/;
    if(!regExp3.test($("#nickName").val())){
        updateCheck.nickName = false;
    }else{
        updateCheck.nickName = true;
    }

    // updateCheck 내부에 저장된 값 검사
    for(var key in updateCheck){
        if(!updateCheck[key]){ // false인 경우
            swal("일부 값이 유효하지 않습니다.");
            return false;
        };
    }
}


// 아이디 찾기 유효성검사
function IdFindValidate(){
    var updateCheck = { "name": false,
                        "email": false}

    // 이름 유효성 검사
    var regExp1 = /^[가-힇]{2,10}$/;
    if(!regExp1.test($("#name").val())){
        updateCheck.name = false;
    }else{
        updateCheck.name = true;
    }

    // 이메일 유효성 검사
    var regExp4 =  /^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/;
    if(!regExp4.test($("#email").val())){
        updateCheck.email = false;
    }else{
        updateCheck.email = true;
    }

    // updateCheck 내부에 저장된 값 검사
    for(var key in updateCheck){
        if(!updateCheck[key]){ // false인 경우
            swal("일부 값이 유효하지 않습니다.");
            return false;
        };
    }
}