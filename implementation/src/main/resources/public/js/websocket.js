/**
 * Serviceworker
 */

// if ('serviceWorker' in navigator) {
//     window.addEventListener('load', function() {
//       navigator.serviceWorker.register('js/sw.js').then(function(registration) {
//         // Registration was successful
//         console.log('ServiceWorker registration successful with scope: ', registration.scope);
//       }, function(err) {
//         // registration failed :(
//         console.log('ServiceWorker registration failed: ', err);
//       });
//     });
// }


var host = "localhost";
var port = 8080;
var endpoint = "notification";

let socket;

if (socket == null) {
    socket = new WebSocket("ws://" + host + ":" + port + "/" + endpoint);
    console.log("Websocket connection established!");
    console.log(socket);

} else {
    console.log("Websocket already connected!");
    console.log(socket);
}

socket.onopen = function (event) {

    console.log("Websocket is connected!");
    // openSnackbar("Websocket is connected!")

}

socket.onmessage = function (message) {

    openSnackbar(message.data)
    setTimeout(function () {
        location.reload();
    }, 3000);

    // openSnackbar(message)

}