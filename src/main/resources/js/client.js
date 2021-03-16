client = (function (){

    var messages = [];

    const postMessage = ( newMessage ) => {
        if (newMessage === "" || ( newMessage.includes('<') && newMessage.includes('>'))){
            swal({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong. Check your input and try again.',
            });
        } else { 
            fetch('/messages', {
                    method: 'POST',
                    headers: {
                    "Content-Type": "text/plain"
                    },
                    body: newMessage
                }).then( res => {
                    if (res.ok) return res.json();
                    return null;
                })
                .catch( () => {})
                .then(  resp  => {
                    messages = [resp, ...messages];
                    showMessages();
                });
        }
    }

    const getMessages = () => {
        fetch('/messages')
            .then( res => {
                if (res.ok) return res.json();
                return null;
            })
            .catch( () => {})
            .then(  resp  => {
                messages = resp;
                showMessages();
            } );
    }

    const showMessages = () => {
        $("#divmessages").empty();
        messages.map( log => {
            $("#divmessages").append(
                `<div class="border border-success text-white mb-4 rounded" style="min-height: 5vh;"><p class="text-success">${log.date}</p><p>${log.message}</p></div>`
            );
        });
    }

    return{
        getMessages: getMessages,
        postMessage: postMessage
    }

})();