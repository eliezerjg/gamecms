import "./styles.css";
import {Link} from "react-router-dom";
import img from "../../../../public/assets/logo2.png";
import React, {useState} from "react";
import ModalLogin from "../ModalLogin/ModalLogin.jsx";
import i18n from "../../../lib/i18n.jsx"


let items = [
    {
        nome: "",
        ref: "/admin/",
        auth: false,
        logo: ""
    }
];

function MenuNavbar() {
    const [isLoggedIn, setIsLoggedIn] = useState(
        localStorage.getItem("admin_isLoggedIn") === "true"
    );


    const [username, setUsername] = useState(localStorage.getItem("admin_username"));

    const handleLogout = () => {
        localStorage.removeItem("admin_isLoggedIn");
        localStorage.removeItem("admin_username");
        localStorage.removeItem("admin_jwt");
        setIsLoggedIn(false);
        setUsername(null);
        window.location = "/admin";
    };

    const handleLoginSuccess = (loginData) => {
        setUsername(loginData.username);
        setIsLoggedIn(true);
        document.querySelector("#modal_entrar > div > a > button").click();
    };

    const siteData = JSON.parse(sessionStorage.getItem('siteData') || `{}`);

    return (
        <div className="h-[20vh]">
            <div className="navbar h-full">
                <div className="navbar-start">
                    <div className="logo-container">
                        <Link to="/admin">
                            <img className="logo" src={siteData && siteData.logoImage} alt="Logo"/>
                        </Link>
                    </div>
                </div>
                <div className="navbar-center lg:flex">
                    {/* Menu Horizontal */}

                    <ul className="menu menu-horizontal px-1">
                        {
                            (!isLoggedIn ? items.filter(item => !item.auth) : items).map((item, index) => (
                                <li key={index}>
                                    <Link to={item.ref}>{item.logo} {item.nome} </Link>
                                </li>
                            ))}
                    </ul>
                    {/* Menu Horizontal */}
                </div>




                <div className="navbar-end lg:flex lg:items-center">
                    {isLoggedIn ? (
                        <div className="w-100">

                            <div className="dropdown dropdown-end">


                                <label tabIndex={1} className="btn btn-ghost rounded-btn">

                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                        <path strokeLinecap="round" strokeLinejoin="round"
                                              d="M17.982 18.725A7.488 7.488 0 0012 15.75a7.488 7.488 0 00-5.982 2.975m11.963 0a9 9 0 10-11.963 0m11.963 0A8.966 8.966 0 0112 21a8.966 8.966 0 01-5.982-2.275M15 9.75a3 3 0 11-6 0 3 3 0 016 0z"/>
                                    </svg>
                                    &nbsp;&nbsp;
                                    {username}&nbsp;
                                </label>


                                {/* DROP DOWN PAINEL*/}
                                <ul
                                    tabIndex={1}
                                    className="menu dropdown-content z-[1] p-2 shadow bg-base-100 rounded-box w-52 mt-4"
                                >

                                    <li>
                                        <Link className="w-full" to="/admin/panel">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                 strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                                <path strokeLinecap="round" strokeLinejoin="round"
                                                      d="M17.982 18.725A7.488 7.488 0 0012 15.75a7.488 7.488 0 00-5.982 2.975m11.963 0a9 9 0 10-11.963 0m11.963 0A8.966 8.966 0 0112 21a8.966 8.966 0 01-5.982-2.275M15 9.75a3 3 0 11-6 0 3 3 0 016 0z"/>
                                            </svg>
                                            Panel
                                        </Link>
                                    </li>
                                    <li>
                                        <a className="w-full" onClick={handleLogout}>
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                 strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                                <path strokeLinecap="round" strokeLinejoin="round"
                                                      d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15M12 9l-3 3m0 0l3 3m-3-3h12.75"/>
                                            </svg>

                                            Logout
                                        </a>
                                    </li>
                                </ul>

                                {/* DROP DOWN PAINEL*/}

                            </div>
                        </div>
                    ) : (
                        <a href="#modal_entrar"
                           className="bg-red-500 hover:bg-orange-700 text-white font-bold py-2 px-4 rounded">
                            {i18n.t('admin.components.MenuNavbar.login')}
                        </a>
                    )}
                </div>


                <div className="modal" id="modal_entrar">
                    <ModalLogin onLoginSuccess={handleLoginSuccess}/>
                </div>


            </div>
        </div>
    );
}

export default MenuNavbar;
