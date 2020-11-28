let accounts={
    regex:{
        email:/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
        password:/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,64}$/
    },
    showHide:function (event){
        event.stopPropagation();
        let targetElem=event.target;
        let input=event.target.previousElementSibling;
        if(targetElem.classList.contains('showeye')){
            input.type='text';
            targetElem.classList.remove("showeye");
            targetElem.classList.add("hideeye");
        }else{
            input.type='password';
            targetElem.classList.remove("hideeye");
            targetElem.classList.add('showeye');
        }
    }
}