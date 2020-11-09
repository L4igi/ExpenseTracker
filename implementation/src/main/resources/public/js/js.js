///// geklaut von https://stackoverflow.com/questions/9899372/pure-javascript-equivalent-of-jquerys-ready-how-to-call-a-function-when-t

function docReady(fn) {
    // see if DOM is already available
    if (document.readyState === "complete" || document.readyState === "interactive") {
        // call on next available tick
        setTimeout(fn, 1);
    } else {
        document.addEventListener("DOMContentLoaded", fn);
    }
}



/////////////////////////////////////////////////////
//////Proxy Patern
async function insertUserImageProxy() {
    function isCached () {
        return window.sessionStorage.getItem("imgUser")==getCookie("id");
    }
    if(!isCached()){
        console.log("about to load userImg from db");
        let imgObj=await getImageIconFromDB(getCookie("id"));
        saveUserImageToCache(imgObj);
    }
    console.log("setting userImg");

    setUserImageFromCache(window.sessionStorage.getItem("userImg"));
}
function saveUserImageToCache(userImgObject) {
    window.sessionStorage.setItem("userImg",userImgObject.userImg);
    window.sessionStorage.setItem("imgUser",userImgObject.imgUser);
    return 0;
}
function setUserImageFromCache(){
    document.getElementById("barUserIcon").src=window.sessionStorage.getItem("userImg");
    document.getElementById("bigAvatar").src=window.sessionStorage.getItem("userImg");
}
async function getImageIconFromDB(userID) {
    var path = "getAllUsers";
    var data_file = "./" + path;
    let response = await fetch(data_file);
    let jsonObj = await response.json();
    for (i in jsonObj) {
        if (jsonObj[i].id == userID) {
            return {"imgUser": userID, "userImg": jsonObj[i].imageIcon};

        }
    }
}
function sleep(milliseconds) {
    return new Promise(resolve => setTimeout(resolve, milliseconds));
}

////

function parse_get(url) { // resolves get variables from the url to a map
    if(url.indexOf("?")==-1)
        return new Map();
    var getpart = url.split("?")[1];
    var getvars = getpart.split("&");
    var map = new Map();
    for (var i = getvars.length - 1; i >= 0; i--) {
        map.set(getvars[i].split("=")[0], getvars[i].split("=")[1]);
    }
    return map;
}


function show_add_user() {
    document.getElementById("createContainer").style.display = "block";
    document.getElementById("loginContainer").style.display = "none";
}

function show_existing_user() {
    document.getElementById("createContainer").style.display = "none";
    document.getElementById("loginContainer").style.display = "block";
    list_users();

}

function show_add_account(argument) { //adding account Modal
    document.getElementById("addAcc").style.display = "block";
}

function show_add_trans(argument) { //adding transaction modal
    document.getElementById("addT").style.display = "block";
}


function add_account(userID, accountName, accountType) {
    add_acc(userID, accountName, accountType);
}

function add_acc(userID, accountName, accountType) { //addingt account to DB
    var userID = getCookie("id");
    var accountName = document.getElementById('accName').value;
    var sI = document.getElementById("accType").selectedIndex;
    var accountType = document.getElementById("accType").options[sI].value;
    var limit = document.getElementById("limit").value;
    var matrNr = document.getElementById("matrNr").value;
    var startAmount = document.getElementById("startAmount").value;
    var dataFile = "";
    if(isNaN(limit)){
        openSnackbar("Limit must be a number");
        return 0;
    }
    if(limit<0){
        openSnackbar("Limit must be greater then 0");
        return 0;
    }
    if(isNaN(matrNr)){
        openSnackbar("Student Number must be a number");
        return 0;
    }
    if(matrNr<0){
        openSnackbar("Student Number must be greater then 0");
        return 0;
    }
    if(isNaN(startAmount)){
        openSnackbar("Start amount must be a number");
        return 0;
    }
    if(startAmount<0){
        openSnackbar("Start amount must be greater then 0");
        return 0;
    }
    if (accountType == "Giro") {
        dataFile = "./newAccount?userID=" + userID + "&accountName=" + accountName + "&accountType=GIRO&overdraftlimit=" + limit;
    }
    if (accountType == "Student") {
        dataFile = "./newAccount?userID=" + userID + "&accountName=" + accountName + "&accountType=STUDENT&overdraftlimit=" + limit + "&studentNumber=" + matrNr;
    }
    if (accountType == "Cash") {
        dataFile = "./newAccount?userID=" + userID + "&accountName=" + accountName + "&accountType=CASH&overdraftlimit=" + limit + "&startAmount=" + startAmount;
    }
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            if(http_request.status==500)
                openSnackbar("Account could not be created. Possible duplicate Accountname.");
            else
                window.open("./overview.html", "_self");
        }
    }
    http_request.open("GET", dataFile, true);
    http_request.send();

}

function create_ktoseite() { //inserting information into the ktoübersicht
    var accID = getAccID();
    var path = "getAllAccounts";
    var data_file = "./" + path + "?userID=" + getCookie("id");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            for (i in jsonObj) {
                if (jsonObj[i].accountID == accID) {
                    document.getElementById("ktotitel").innerHTML = jsonObj[i].accountName;
                    document.getElementById("ktostand").innerHTML = jsonObj[i].balance;
//                    document.getElementById("ktotyp").innerHTML = jsonObj[i].accountType;
                    list_transactions(accID);
                }
            }
            //window.open("./overview.html?id="+jsonObj.id+"&username="+jsonObj.username,"_self");
        }
    }
    http_request.open("GET", data_file, true);
    http_request.send();
}

function add_t_to_list(tID, tDate, tDesc, tAmount, tCat, tType, tIcon) { // addingt transaction to Transactionlist
    tAmount = Math.round(tAmount * 100) / 100;
    var sortBy = parse_get(document.location.href).get("fCatKonto");
    if (sortBy != undefined) {
        if (sortBy == "PRIVATE" || sortBy == "BUSINESS" || sortBy == "CRYPTO") {
            if (sortBy != tType)
                return 0;
        } else if (sortBy == "INCOMING" || sortBy == "OUTGOING") {
            if (sortBy == "INCOMING") {
                if (tAmount < 0) {
                    return 0;
                }
            }
            if (sortBy == "OUTGOING") {
                if (tAmount > 0) {
                    return 0;
                }
            }
        } else if (tCat != sortBy) {
            return 0;
        }
    }
    var tList = document.getElementById("tList");
    tList.innerHTML +=
        `
        <li class="w3-bar">
            <div class="w3-bar-item" style="width:50px;">${tIcon}</div>
            <div class="w3-bar-item" style="width:200px;">${tCat}</div>
            <div class="w3-bar-item">
                <span>${tDesc}</span><br>
                <span>${("0" + tDate.getDate()).slice(-2) + "." + ("0" + (1 + tDate.getMonth())).slice(-2) + "." + tDate.getFullYear()}</span>
            </div>
            <div class="w3-bar-item w3-right">${tAmount} €</div>
        </li>
    `;


}

function list_transactions(accID) { // initiating the transaction listing by gathering the info, calling function to actually list them
    var path = "getAllTransactionsByAccountID";
    var data_file = "./" + path + "?accountID=" + accID;
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            for (var i = 0; i < jsonObj.length; i++) {
                jsonObj[i].transactionDate = new Date(jsonObj[i].transactionDate);
                jsonObj[i].transactionDate.setHours(0);
            }
            jsonObj.sort(function (a, b) {
                return b.transactionDate.getTime() - a.transactionDate.getTime();
            });
            for (i in jsonObj) {
                if (jsonObj[i].fromAccountID == accID) {
                    jsonObj[i].amount = (-1 * jsonObj[i].amount);
                }
            }
            jsonObj = filterTransactions(jsonObj, fromDateKonto(), toDateKonto(), document.getElementById("fCatKonto").value, document.getElementById("fTypKonto").value, document.getElementById("fBalKonto").value)
            document.getElementById("tList").innerHTML = "";
            for (i in jsonObj) {
                add_t_to_list(jsonObj[i].transactionID, jsonObj[i].transactionDate, jsonObj[i].transactionDescription, jsonObj[i].amount, jsonObj[i].transactionCategory, jsonObj[i].transactionType, jsonObj[i].transactionCategoryIcon);
            }
            //window.open("./overview.html?id="+jsonObj.id+"&username="+jsonObj.username,"_self");
        }
    }
    http_request.open("GET", data_file, true);
    http_request.send();
}

function list_accounts(userID) {// initiating the account listing by gathering the info, calling function to actually list them
    var path = "getAllAccounts";
    var data_file = "./" + path + "?userID=" + userID;
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            let jsonObj = JSON.parse(http_request.responseText);
            for (i in jsonObj) {
                if(document.getElementById("filterAllTransactionsButton")!=null)
                    document.getElementById("filterAllTransactionsButton").style.display = "block";
                document.getElementById("addTransactionButton").style.display = "block";
                add_acc_to_list(userID, jsonObj[i]);
            }
            if(getAccID()!=undefined)
                document.getElementById("kto" +getAccID()).classList += " w3-theme-l4 ";
        }
    }
    http_request.open("GET", data_file, true);
    http_request.send();

}
function add_acc_to_list (uID, accObj) {
    var accID =accObj.accountID;
    var titel = accObj.accountName;
    var typ =accObj.accountType;
    var betrag = accObj.balance;
    var matrNr =accObj.student_number;
    if(accObj.student_number==undefined)
        matrNr = "";


    // document.onreadystatechange = function () {
        // if (document.readyState == "complete") {
            // document is ready. Do your stuff here
            if(document.getElementById("konten")!=undefined){
                document.getElementById("accountCard").style.display="block";

                document.getElementById("konten").innerHTML +=
                `
                    <a href = './konto.html?AccID=${accID}'>
                        <div id="kto${accID}" class="w3-container w3-white w3-round w3-margin w3-row w3-border-left w3-border-right">
                            <div class="w3-half">
                                <h3>${titel}</h3>
                                <h5>${typ+" "+matrNr}</h5>
                            </div>            
                            <div class="w3-half w3-right-align">
                                <h4 class="kontenBetrag">${parseInt(betrag*100)/100} €</h4>
                            </div>
                        </div>
                    </a>
                `;
            }

            if(document.getElementById("accountSideCard")!=undefined){
                document.getElementById("accountSideCard").style.display="block";
                document.getElementById("accountSideCard").innerHTML+=

                `
                    <a href = "./konto.html?AccID=${accID}">
                        <div id="kto${accID}" class="w3-container w3-white w3-round w3-margin w3-row w3-border-left w3-border-right">
                            <p>${titel}</p>
                            <p>${parseInt(100*betrag)/100+" €"}</p>
                        </div>
                    </a>
                `;
            }
        // };
    // }
}

async function add_trans_from_form() { // preparing information to add transaction to db & calling function to actually add it
    var uID = getCookie("id");
    var fAccID = document.getElementById('fAcc').value;
    var tAccID = document.getElementById('tAcc').value;
    var amount = document.getElementById('amount').value;
    var tDate = document.getElementById('newTransactionDate').value;
    var tDescription = document.getElementById('newTransactionTitle').value;
    var sI = document.getElementById("tCat").selectedIndex;
    var transactionCategory = document.getElementById("tCat").options[sI].value;
    var i = document.getElementById("tTyp").selectedIndex;
    var transactionType = document.getElementById("tTyp").options[i].value;
    var validOverdraft = true;
    if(isNaN(amount)){
        openSnackbar("Transaction amount must be a number");
        return 0;
    }
    if(amount==""||parseInt(amount)==0){
        openSnackbar("Transaction amount must not be 0 or empty");
        return 0;
    }

    if(amount<0){
        openSnackbar("Transaction amount must be greater then 0");
        return 0;
    }
    if (fAccID == undefined)
        validOverdraft = await isTransactionValidOverdraft(amount, fAccID, uID);
    if (validOverdraft)
        add_transaction(uID, fAccID, tAccID, amount, tDate, encodeURIComponent(tDescription), transactionCategory, transactionType);
    else
        openSnackbar("overdraft exceeded");
}
function add_trans(uID, fAccID, tAccID, amount, tDate, tDescription, transactionCategory, transactionType) { //adding transaction to db
    if (fAccID == "")
        fAccID = 1;
    if (tAccID == "")
        tAccID = 1;
    var path = "newTransaction";
    data_file = "./" + path + "?fromUser=" + uID + "&toUser=" + uID + "&fromAccount=" + fAccID + "&toAccount=" + tAccID + "&amount=" + amount + "&date=" + tDate + "&description=" + tDescription + "&transactionCategory=" + transactionCategory + "&transactionType=" + transactionType;
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
        }
    }
    http_request.open("GET", data_file, true);
    http_request.send();
}

function add_transaction(uID, fAccID, tAccID, amount, tDate, tDescription, transactionCategory, transactionType) { //adding transaction to db
    add_trans(uID, fAccID, tAccID, amount, tDate, tDescription, transactionCategory, transactionType);
    // location.reload();
    return false;
}

//setCookie und getCookie von https://www.w3schools.com/js/js_cookies.asp kopiert.
function setCookie(cname, cvalue, exdays = 1) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

//getAllAccounts?userID=2
async function getAllAccountsForTransaction(userID) {
    let allAccs = await getAllAccounts(userID);
    for (i in allAccs) {
        listAccountsForTransactions(userID, allAccs[i]);
    }
}

//.accountID,jsonObj[i].accountName,jsonObj[i].accountType,jsonObj[i].balance
function listAccountsForTransactions(userID, accountObj) {
    document.getElementById("fAcc").innerHTML += `<option value ="${accountObj.accountID}" >${accountObj.accountName}</option>`;
    document.getElementById("tAcc").innerHTML += `<option value ="${accountObj.accountID}" >${accountObj.accountName}</option>`;
}

function accountSelect() { // display input elements depending on account type selection
    var sI = document.getElementById("accType").selectedIndex;
    var accountType = document.getElementById("accType").options[sI].value;
    if (accountType == "Student") {
        document.getElementById("matrNr").style.display = "block";
        document.getElementById("limit").style.display = "block";
        document.getElementById("startAmount").style.display = "none";
    }
    if (accountType == "Giro") {
        document.getElementById("matrNr").style.display = "none";
        document.getElementById("limit").style.display = "block";
        document.getElementById("startAmount").style.display = "none";
    }
    if (accountType == "Cash") {
        document.getElementById("matrNr").style.display = "none";
        document.getElementById("limit").style.display = "none";
        document.getElementById("startAmount").style.display = "block";
    }
}

function createTestData() {
    var accTypes = ["GIRO", "STUDENT", "CASH"];

    for (var i = 2; i < 12; i++) {
        add_account(i, Math.random().toString(36).substring(7), accTypes[i % 3]);
    }
    for (var i = 0; i < 102; i++) {
        add
    }

}

async function isTransactionValidOverdraft(amount, accID, uID) {
    let remaining = await accountGetRemainingSpending(uID, accID);
    if ((remaining - amount) >= 0)
        return true;
    return false;
}

async function accountGetRemainingSpending(uID, accID) {
    let acc = await getAccount(uID, accID);
    return acc.balance + acc.overdraftlimit;
}

async function getAllAccounts(userID) {
    var path = "getAllAccounts";
    var data_file = "./" + path + "?userID=" + userID;
    let response = await fetch(data_file);
    let allAccs = await response.json();
    return allAccs;
}

async function getAccount(userID, accID) {
    const allAccs = await getAllAccounts(userID);
    for (var i = 0; i < allAccs.length; i++) {
        if (allAccs[i].accountID == accID)
            return allAccs[i];
    }
    t.tran
}

//----------------------------
function getAccountByUserIDAndAccountID(UserID, AccountID) {
    var path = "getAllAccounts";
    var data_file = "./" + path + "?userID=" + userID;
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            for (i in jsonObj) {
                listAccountsForTransactions(userID, jsonObj[i]);
            }
            //window.open("./overview.html?id="+jsonObj.id+"&username="+jsonObj.username,"_self");
        }
    }
    http_request.open("GET", data_file, true);
    http_request.send();
}


//summary help functions


/////////////////
function fillFilterY1() {
    var path = "./getYearsByUserID?userID=" + getCookie("id");
    var fy = document.getElementById("fromYear");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var y = jsonObj;
            y.year = y.year.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            if(y.year.length==0){
                document.getElementById("filterAllTransactionsButton").style.display = "none";
                return 0;
            }
            for (var i = 0; i < y.year.length; i++) {
                if (i == 0)
                    var selected = "selected";
                else
                    var selected = "";
                inHtml += `<option ${selected} value="${y.year[i]}" selected>${y.year[i]}</option>`;
            }

            fy.innerHTML = inHtml;
            fillFilterM1();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterM1() {
    var path = "./getMonthsByYearByUserID?userID=" + getCookie("id") + "&year=" + document.getElementById("fromYear").value;
    var fm = document.getElementById("fromMonth");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var m = jsonObj;
            m.month = m.month.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < m.month.length; i++) {
                inHtml += `<option value="${m.month[i]}" selected>${m.month[i]}</option>`;
            }
            fm.innerHTML = inHtml;
            fillFilterD1();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterD1() {
    var path = "./getDaysByMonthsByYearByUserID?userID=" + getCookie("id") + "&year=" + document.getElementById("fromYear").value + "&month=" + document.getElementById("fromMonth").value;
    var fd = document.getElementById("fromDay");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var d = jsonObj;
            d.day = d.day.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < d.day.length; i++) {
                inHtml += `<option value="${d.day[i]}" selected>${d.day[i]}</option>`;
            }
            fd.innerHTML = inHtml;
            fillFilterY2();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}


function fillFilterY2() {
    var path = "./getYearsByUserID?userID=" + getCookie("id");
    var fy = document.getElementById("toYear");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var y = jsonObj;
            y.year = y.year.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < y.year.length; i++) {
                if (parseInt(y.year[i]) >= parseInt(document.getElementById("fromYear").value))
                    inHtml += `<option value="${y.year[i]}" selected>${y.year[i]}</option>`;
            }

            fy.innerHTML = inHtml;
            fillFilterM2();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterM2() {
    var path = "./getMonthsByYearByUserID?userID=" + getCookie("id") + "&year=" + document.getElementById("toYear").value;
    var tm = document.getElementById("toMonth");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var m = jsonObj;
            m.month = m.month.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < m.month.length; i++) {
                if (document.getElementById("toYear").value > document.getElementById("fromYear").value)
                    inHtml += `<option value="${m.month[i]}" selected>${m.month[i]}</option>`;
                else if (parseInt(m.month[i]) >= parseInt(document.getElementById("fromMonth").value))
                    inHtml += `<option value="${m.month[i]}" selected>${m.month[i]}</option>`;
            }
            tm.innerHTML = inHtml;
            fillFilterD2();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterD2() {
    var path = "./getDaysByMonthsByYearByUserID?userID=" + getCookie("id") + "&year=" + document.getElementById("toYear").value + "&month=" + document.getElementById("toMonth").value;
    var td = document.getElementById("toDay");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            d = jsonObj;
            d.day = d.day.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < parseInt(d.day.length); i++) {
                if (parseInt(document.getElementById("toYear").value) > parseInt(document.getElementById("fromYear").value))
                    inHtml += `<option value="${parseInt(d.day[i])}" selected>${parseInt(d.day[i])}</option>`;
                else if (parseInt(document.getElementById("toMonth").value) > parseInt(document.getElementById("fromMonth").value))
                    inHtml += `<option value="${parseInt(d.day[i])}" selected>${parseInt(d.day[i])}</option>`;
                else if (parseInt(d.day[i]) >= parseInt(document.getElementById("fromDay").value))
                    inHtml += `<option value="${parseInt(d.day[i])}" selected>${parseInt(d.day[i])}</option>`;
            }
            td.innerHTML = inHtml;
            listAllFilteredTransactions();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fromDate() {
    return new Date(
        document.getElementById("fromYear").value,
        document.getElementById("fromMonth").value - 1,
        document.getElementById("fromDay").value,
    )
}

function toDate() {
    return new Date(
        document.getElementById("toYear").value,
        document.getElementById("toMonth").value - 1,
        document.getElementById("toDay").value
    )
}

function setAccID() {
    if(parse_get(window.location.href).get("AccID")!=undefined){
        window.sessionStorage.setItem("lastAccID",parse_get(window.location.href).get("AccID"));
    }
}
function getAccID() {
    if(parse_get(window.location.href).get("AccID")==undefined){
        return window.sessionStorage.getItem("lastAccID");
    }
    else
        return parse_get(window.location.href).get("AccID");
}

/////////////////

function fillSelectMonthGraph(year) {
    var path = "./getMonthsByYearByUserID?userID=" + getCookie("id") + "&accountID=" + getAccID() + "&year=" + year;
    var gm = document.getElementById("graphMonths");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var m = jsonObj;
            m.month = m.month.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < m.month.length; i++) {
                inHtml += `<option value="${""+year+"/"+m.month[i]}" selected>${""+year+"/"+m.month[i]}</option>`;
            }
            gm.innerHTML += inHtml;
            launchLineChart();

        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterY1Konto() {
    var path = "./getYearsByUserID?userID=" + getCookie("id") + "&accountID=" +getAccID();
    var fy = document.getElementById("fromYearKonto");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var y = jsonObj;
            y.year = y.year.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            var selectUnfilled = false;
            if(document.getElementById("graphMonths").value=="")
                selectUnfilled = true;
            if(y.year.length==0){
                document.getElementById("transFilterKonto").style.display = "none";
                document.getElementById("filterAllTransactionsButton").style.display = "none";
                return 0;
            }
            for (var i = 0; i < y.year.length; i++) {
                if (i == 0)
                    var selected = "selected";
                else
                    var selected = "";
                inHtml += `<option ${selected} value="${y.year[i]}" selected>${y.year[i]}</option>`;
                if(selectUnfilled)
                    fillSelectMonthGraph(y.year[i]);
            }

            fy.innerHTML = inHtml;
            fillFilterM1Konto();
            list_transactions(getAccID());

        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterM1Konto() {
    var path = "./getMonthsByYearByUserID?userID=" + getCookie("id") + "&accountID=" + getAccID() + "&year=" + document.getElementById("fromYearKonto").value;
    var fm = document.getElementById("fromMonthKonto");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var m = jsonObj;
            m.month = m.month.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < m.month.length; i++) {
                inHtml += `<option value="${m.month[i]}" selected>${m.month[i]}</option>`;
            }
            fm.innerHTML = inHtml;
            fillFilterD1Konto();
            fillFilterM2Konto();
            list_transactions(getAccID());

        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterD1Konto() {
    var path = "./getDaysByMonthsByYearByUserID?userID=" + getCookie("id") + "&accountID=" + getAccID() + "&year=" + document.getElementById("fromYearKonto").value + "&month=" + document.getElementById("fromMonthKonto").value;
    var fd = document.getElementById("fromDayKonto");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var d = jsonObj;
            d.day = d.day.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < d.day.length; i++) {
                inHtml += `<option value="${d.day[i]}" selected>${d.day[i]}</option>`;
            }
            fd.innerHTML = inHtml;
            fillFilterY2Konto();
            fillFilterD2Konto();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}


function fillFilterY2Konto() {
    var path = "./getYearsByUserID?userID=" + getCookie("id") + "&accountID=" +getAccID();
    var fy = document.getElementById("toYearKonto");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var y = jsonObj;
            y.year = y.year.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < y.year.length; i++) {
                if (y.year[i] >= document.getElementById("fromYearKonto").value)
                    inHtml += `<option value="${y.year[i]}" selected>${y.year[i]}</option>`;
            }

            fy.innerHTML = inHtml;
            fillFilterM2Konto();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterM2Konto() {
    if(document.getElementById("toYearKonto").value=="")
        return 0;
    var path = "./getMonthsByYearByUserID?userID=" + getCookie("id") + "&accountID=" +getAccID()+ "&year=" + document.getElementById("toYearKonto").value;
    var tm = document.getElementById("toMonthKonto");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var m = jsonObj;
            m.month = m.month.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < m.month.length; i++) {
                if (parseInt(document.getElementById("toYearKonto").value) > parseInt(document.getElementById("fromYearKonto").value))
                    inHtml += `<option value="${m.month[i]}" selected>${m.month[i]}</option>`;
                else if (parseInt(m.month[i]) >= parseInt(document.getElementById("fromMonthKonto").value))
                    inHtml += `<option value="${m.month[i]}" selected>${m.month[i]}</option>`;
            }
            tm.innerHTML = inHtml;
            fillFilterD2Konto();
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fillFilterD2Konto() {
    if(document.getElementById("toMonthKonto").value=="")
        return 0;
    var path = "./getDaysByMonthsByYearByUserID?userID=" + getCookie("id") + "&accountID=" +getAccID()+ "&year=" + document.getElementById("toYearKonto").value + "&month=" + document.getElementById("toMonthKonto").value;
    var td = document.getElementById("toDayKonto");
    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            var inHtml = "";
            var d = jsonObj;
            d.day = d.day.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (var i = 0; i < d.day.length; i++) {
                if (parseInt(document.getElementById("toYearKonto").value )> parseInt(document.getElementById("fromYearKonto").value))
                    inHtml += `<option value="${d.day[i]}" selected>${d.day[i]}</option>`;
                else if (parseInt(document.getElementById("toMonthKonto").value) > parseInt(document.getElementById("fromMonthKonto").value))
                    inHtml += `<option value="${d.day[i]}" selected>${d.day[i]}</option>`;
                else if (parseInt(d.day[i]) >= parseInt(document.getElementById("fromDayKonto").value))
                    inHtml += `<option value="${d.day[i]}" selected>${d.day[i]}</option>`;
            }
            td.innerHTML = inHtml;
            list_transactions(getAccID());
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function fromDateKonto() {
    return new Date(
        document.getElementById("fromYearKonto").value,
        document.getElementById("fromMonthKonto").value - 1,
        document.getElementById("fromDayKonto").value
    )
}

function toDateKonto() {
    return new Date(
        document.getElementById("toYearKonto").value,
        document.getElementById("toMonthKonto").value - 1,
        document.getElementById("toDayKonto").value
    )
}

////////////////////

function listAllFilteredTransactions() {
    var path = "./getAllTransactionByUserID?userID=" + getCookie("id");
    var http_request = new XMLHttpRequest();
    var allTransactions;
    var ft;
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            allTransactions = filterTransactions(JSON.parse(http_request.responseText), fromDate(), toDate(), document.getElementById("fCat").value, document.getElementById("fTyp").value, document.getElementById("fBal").value);
            document.getElementById("filteredTransactionList").innerHTML = "";
            var barplotData = {};
            if (allTransactions.length == 0) {
                document.getElementById("filterAllTransactionsButton").style.display = "none";
                document.getElementById("transFilter").style.display = "none";
                document.getElementById("transFilterChart").style.display = "none";
                return 0;
            }
            for (var i = 0; i < allTransactions.length; i++) {
                var a = allTransactions;
                if (barplotData[a[i].transactionCategory] == undefined)
                    barplotData[a[i].transactionCategory] = {income: 0, expense: 0};
                if (a[i].amount > 0)
                    barplotData[a[i].transactionCategory].income += a[i].amount;
                if (a[i].amount < 0)
                    barplotData[a[i].transactionCategory].expense -= a[i].amount;
                document.getElementById("filteredTransactionList").innerHTML += `
                    <li class="w3-padding-16">
                        <div class="w3-row">
                            <div class="w3-col" style="width:50px;">${a[i].transactionCategoryIcon}</div>
                            <div class="w3-quarter">
                                <div>${a[i].transactionDate}</div>
                                <div>${a[i].transactionDescription}</div>
                            </div>
                            <div class="w3-quarter">
                                <table class="">
                                    <tr>
                                    <td></td>
                                    <td>User</td>
                                    <td>Account</td>
                                    </tr>
                                    <tr>
                                        <td>From:</td>
                                        <td>${a[i].fromUserID}</td>
                                        <td>${a[i].fromAccountID}</td>                    
                                    </tr>
                                    <tr>
                                        <td>To:</td>
                                        <td></td>
                                        <td>${a[i].toAccountID}</td>
                                    </tr>
                                </table>
                            </div>
                            <div class="w3-rest w3-left">
                                ${parseInt((a[i].amount) * 100) / 100}€
                            </div>
                         </div>
                    </li>`;
            }
            drawBarplot(barplotData);
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}

function filterTransactions(transactionList, startDate, endDate, catVal, typVal, balance) {
    returnList = [];
    for (var i = 0; i < transactionList.length; i++) {
        if (transactionList[i].fromUserID == getCookie("id"))
            transactionList[i].amount = transactionList[i].amount * -1;
        var transactionDate = new Date(transactionList[i].transactionDate);
        transactionDate.setHours(0);
        if ((transactionDate >= startDate) && (transactionDate <= endDate))
            if (catVal == "" || catVal == transactionList[i].transactionCategory)
                if (typVal == "" || typVal == transactionList[i].transactionType)
                    if (balance == "" || (balance == "INCOME" && transactionList[i].amount > 0) || (balance == "EXPENSE" && transactionList[i].amount < 0))
                        returnList.push(transactionList[i]);
    }
    return returnList;
}

function launchFilter() {
    document.getElementById("transFilter").style.display = "block";
    document.getElementById("transFilterChart").style.display = "block";
    fillFilterY1()
}




function drawBarplot(inData) {
    document.getElementById('barplot').innerHTML = "";
// set the dimensions and margins of the graph
    var margin = {top: 10, right: 30, bottom: 20, left: 50},
        width = 460 - margin.left - margin.right,
        height = 400 - margin.top - margin.bottom;

// append the svg object to the body of the page
    var svg = d3.select("#barplot")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");
    var csvData = "group,income,expense\n"
    for (var i = 0; i < Object.keys(inData).length; i++) {
        csvData += Object.keys(inData)[i] + "," + inData[Object.keys(inData)[i]].income + "," + inData[Object.keys(inData)[i]].expense + "\n";
    }
// Parse the Data
    var a = function (data) {
        data = d3.csvParse(data);
        var maxval = 0;
        for (var i = 0; i < data.length; i++) {
            if (maxval < parseInt(data[i].income)) {
                maxval = parseInt(data[i].income);
            }
            if (maxval < parseInt(data[i].expense)) {
                maxval = parseInt(data[i].expense);

            }
        }
        // List of subgroups = header of the csv files = soil condition here
        var subgroups = data.columns.splice(1);
        // List of groups = species here = value of the first column called group -> I show them on the X axis
        var groups = d3.map(data, function (d) {
            return (d.group)
        }).keys()
        // Add X axis
        var x = d3.scaleBand()
            .domain(groups)
            .range([0, width])
            .padding([0.2])
        svg.append("g")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x).tickSize(0));

        // Add Y axis
        var y = d3.scaleLinear()
            .domain([0, maxval])
            .range([height, 0]);
        svg.append("g")
            .call(d3.axisLeft(y));

        // Another scale for subgroup position?
        var xSubgroup = d3.scaleBand()
            .domain(subgroups)
            .range([0, x.bandwidth()])
            .padding([0.05])

        // color palette = one color per subgroup
        var color = d3.scaleOrdinal()
            .domain(subgroups)
            .range(['#377eb8', '#e41a1c'])

        // Show the bars
        svg.append("g")
            .selectAll("g")
            // Enter in data = loop group per group
            .data(data)
            .enter()
            .append("g")
            .attr("transform", function (d) {
                return "translate(" + x(d.group) + ",0)";
            })
            .selectAll("rect")
            .data(function (d) {
                return subgroups.map(function (key) {
                    return {key: key, value: d[key]};
                });
            })
            .enter().append("rect")
            .attr("x", function (d) {
                return xSubgroup(d.key);
            })
            .attr("y", function (d) {
                return y(d.value);
            })
            .attr("width", xSubgroup.bandwidth())
            .attr("height", function (d) {
                return height - y(d.value);
            })
            .attr("fill", function (d) {
                return color(d.key);
            });

    }
    a(csvData);

}
function launchLineChart() {
    var month = document.getElementById("graphMonths").value.substring(5);
    var year = document.getElementById("graphMonths").value.substring(0,4);
    var path = `./getBalanceByMonthByYearByAccountID?userID=${getCookie("id")}&year=${year}&month=${month}&accountID=${getAccID()}`;

    var http_request = new XMLHttpRequest();
    http_request.onreadystatechange = function () {
        if (http_request.readyState == 4) {
            var jsonObj = JSON.parse(http_request.responseText);
            document.getElementById('lineChart').innerHTML="";
            drawLineChart(jsonObj);
        }
    }
    http_request.open("GET", path, true);
    http_request.send();
}
function drawLineChart(inData) {
// set the dimensions and margins of the graph
    var margin = {top: 10, right: 30, bottom: 30, left: 60},
        width = 800 - margin.left - margin.right,
        height = 400 - margin.top - margin.bottom;
// append the svg object to the body of the page
    var svg = d3.select("#lineChart")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");
    var maxval = parseInt(inData[Object.keys(inData)[1]]);
    var minval = parseInt(inData[Object.keys(inData)[1]]);
    var csvData = "date,value\n"
    for (var i = 0; i < Object.keys(inData).length; i++) {
        var value = parseInt(inData[Object.keys(inData)[i]]);
        csvData += Object.keys(inData)[i] + "," + value+ "\n";
        if(value<minval)
            minval = value;
        if(value>maxval)
            maxval = value;
    }
    maxval = maxval + Math.abs((maxval-minval)*0.1)
    minval = minval - Math.abs((maxval-minval)*0.1)

//Read the data

    var a = function (data) {
        data = d3.csvParse(data);
            // Add X axis --> it is a date format
            var x = d3.scaleLinear()
                .domain([1,31])
                .range([ 0, width ]);
            svg.append("g")
                .attr("transform", "translate(0," + height + ")")
                .call(d3.axisBottom(x));
            // Add Y axis
            var y = d3.scaleLinear()
                .domain([minval,maxval])
                .range([ height, 0 ]);
            svg.append("g")
                .call(d3.axisLeft(y));
            // Add the line
            svg.append("path")
                .datum(data)
                .attr("fill", "none")
                .attr("stroke", "#69b3a2")
                .attr("stroke-width", 1.5)
                .attr("d", d3.line()
                    .x(function(d) { return x(d.date) })
                    .y(function(d) { return y(d.value) })
                )
            // Add the points
        }
    a(csvData);
}
