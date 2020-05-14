// const ajax = (method, url) => {
//     const httpRequest = new XMLHttpRequest();
//
//     if (!httpRequest) {
//         alert('XMLHTTP 인스턴스를 만들 수가 없습니다.');
//         return false;
//     }
//
//     httpRequest.onreadystatechange = () => {
//         if (httpRequest.readyState === XMLHttpRequest.DONE) {
//             if (httpRequest.status === 200) {
//                 const data = httpRequest.responseText;
//                 console.log(data);
//                 return data;
//             } else {
//                 alert('request에 문제가 생겼습니다.');
//             }
//         }
//     };
//     httpRequest.open(method, url);
//     const header = $("meta[name='_csrf_header']").attr("content");
//     const token = $("meta[name='_csrf']").attr("content");
//     httpRequest.setRequestHeader(header, token);
//     httpRequest.send();
// };

// const ajax = async (method, url, data, property) => {
//     const header = $("meta[name='_csrf_header']").attr("content");
//     const token = $("meta[name='_csrf']").attr("content");
//
//     let response;
//     try {
//         if (data == null) {
//             response = await fetch(url, {
//                 method: method,
//                 headers: {
//                     header: token
//                 }
//             });
//         } else {
//             response = await fetch(url, {
//                 method: method,
//                 headers: {
//                     'Content-Type': 'application/json',
//                     header: token
//                 },
//                 body: JSON.stringify(data)
//             });
//         }
//         const responseData = await response.json();
//
//         if (property == null) {
//             return responseData;
//         }
//         return responseData[property];
//     } catch (error) {
//         console.log("Error:", error);
//     }
//
// };
