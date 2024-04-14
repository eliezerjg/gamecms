import Swal from "sweetalert2";

class Request{
    verify = (request, isAdmin) => {
        let logout = () => {
            localStorage.removeItem("isLoggedIn");
            localStorage.removeItem("username");
            localStorage.removeItem("jwt");
            localStorage.removeItem("admin_jwt");
            localStorage.removeItem("admin_isLoggedIn");
            localStorage.removeItem("admin_username");
            location.href = isAdmin ? "/admin" : "/";
        };

        if(request.response.data.message === "Token inválido, faça login novamente."){
            logout();
        }
    };
}

const RequestValidator = new Request();



export default RequestValidator;

