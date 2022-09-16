function searchByKeyword(){
    const type = $('#searchType').val();
    const keyword = $('#searchKeyword').val();

    if(keyword.trim() != "") {
        let url = "?searchType=" + type + "&keyword=" + keyword;
        window.location.href = url;
    } else {
        alert("검색어를 입력해주세요.");

        document.getElementById('searchKeyword').value = null;
    }
}

function filterByPrice() {
    const minRegex = /minPriceFilter=[0-9]{1,}/g;
    const maxRegex = /maxPriceFilter=[0-9]{1,}/g;
    var url = window.location.href;
    var minChanged =  url.match(minRegex);
    var maxChanged =  url.match(maxRegex);
    var minPriceFilter = document.getElementById('minPriceFilter').value;
    var maxPriceFilter = document.getElementById('maxPriceFilter').value;

    if(minPriceFilter<0 || maxPriceFilter<0) {
        alert("0원 이상을 입력해주세요.");

        document.getElementById('minPriceFilter').value = null;
        document.getElementById('maxPriceFilter').value = null;
    } else if(maxPriceFilter<minPriceFilter) {
        alert("최고가는 최소가보다 작을 수 없습니다.");

        document.getElementById('minPriceFilter').value = null;
        document.getElementById('maxPriceFilter').value = null;
    } else {
        if(minPriceFilter!="") {
            url = createUrl("minPriceFilter", minPriceFilter, minChanged, url);
        }
        if(maxPriceFilter!=""){
            url = createUrl("maxPriceFilter", maxPriceFilter, maxChanged, url);
        }

        window.location.href = url;
    }

    // if(minPriceFilter!="") {
    //     url = createUrl("minPriceFilter", minPriceFilter, minChanged, url);
    // }
    // if(maxPriceFilter!=""){
    //     url = createUrl("maxPriceFilter", maxPriceFilter, maxChanged, url);
    // }
    //
    // window.location.href = url;
}

function filterByReviewAvg() {
    const regex = /reviewFilter=[0-9].?[0-9]{0,}/g;
    var url = window.location.href;
    var changed =  url.match(regex);
    var reviewFilter = document.getElementById('reviewFilter').value;

    if(reviewFilter<0 || reviewFilter>5) {
        alert("0점 이상 5점 이하를 입력해주세요.");

        document.getElementById('reviewFilter').value = null;
    } else {
        if (reviewFilter != "") {
            url = createUrl("reviewFilter", reviewFilter, changed, url);
        }

        window.location.href = url;
    }

    // url = createUrl("reviewFilter", reviewFilter, changed, url);
    //
    // window.location.href = url;
}

function filterByCategory(category) {
    const regex = /categoryFilter=[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,}/g;
    var url = decodeURI(window.location.href);
    var changed =  url.match(regex);

    url = createUrl("categoryFilter", category, changed, url);

    window.location.href = url;
}

function orderByInput(input) {
    const regex = /sort=[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,}/g;
    var url = decodeURI(window.location.href);
    var changed =  url.match(regex);

    url = createUrl("sort", input, changed, url);

    window.location.href = url;
}

function moveToPage(num) {
    const regex = /page=[0-9]{1,}/g;
    var url = window.location.href;
    var changed = url.match(regex);

    url = createUrl("page", num, changed, url);

    window.location.href = url;
}

function createUrl(key, value, changed, url){
    if (!url.includes("?")) {
        url += "?" + key + "=" + value;
    }
    else {
        if (changed == null){
            url += "&" + key + "=" + value;
        }else{
            url = url.replace(changed, key + "=" + value);
        }
    }

    return url;
}

function logout() {
    document.cookie = 'token' +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    if (window.location.href != '/' ) {
        window.location.href='/';
    }
    if (window.location.href == '/' ) {
        location.reload();
    }
    alert('정상적으로 로그아웃이 되었습니다.');
}

// 1.?가 있다면
// '?속성=' 추가

// 2.?가 없다면
// 2-1. 이미 속성에 대한 값이 있다면
// replace 진행
// 2-2. 속성에 대한 값이 없다면
// '&속성=' 추가