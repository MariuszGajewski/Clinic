
var content=document.getElementById("page_content");
content.innerHTML=document.getElementById("page1_desc").innerHTML;
function change_tab(id) {
    content.innerHTML=document.getElementById(id+"_desc").innerHTML;
    document.getElementById("page1").className="unselected";
    document.getElementById("page2").className="unselected";
    document.getElementById("page3").className="unselected";
    document.getElementById(id).className="selected";
}

function setDate(text){
    alert("fs");
    var inp =  document.getElementById("da");
    inp.text="sdaffsa";

}

function refresh() {
    setTimeout("window.location.reload(true);",1000);

}

function confirmDel(value) {
    var deleteExam = window.confirm("czy na pewno chcesz odwołać badanie?");
    if(deleteExam == true) {
        var str='/patientZone/delete/' + value;
        location.href=str;
    }
    else{
        location.href ='/patientZone';
    }
}