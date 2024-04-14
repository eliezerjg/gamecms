import "./styles.css";
import {Link} from "react-router-dom";
import ModalLogin from "../ModalLogin/ModalLogin";
import React, {useEffect, useState} from "react";
import BrandingLine from "../BrandingLine/BrandingLine";
import i18n from "../../../lib/i18n.jsx";
import LanguageSwitcher from "../LanguageSwitcher/LanguageSwitcher.jsx";

let itens = [
    {
        nome: i18n.t('site.components.MenuNavbar.items.Home'),
        ref: "/",
        auth: false,
        logo: ""
    },

    {
        nome: i18n.t('site.components.MenuNavbar.items.News'),
        ref: "/news",
        auth: false,
        logo: ""

    },
    {
        nome: i18n.t('site.components.MenuNavbar.items.Register'),
        ref: "/register",
        auth: false,
        logo: ""
    },
    {
        nome: i18n.t('site.components.MenuNavbar.items.Download'),
        ref: "/download",
        auth: false,
        logo: ""
    },
    {
        nome: i18n.t('site.components.MenuNavbar.items.Ranking'),
        ref: "/ranking",
        auth: false,
        logo: ""
    },
    {
        nome: i18n.t('site.components.MenuNavbar.items.Shop'),
        ref: "/shop",
        auth: false,
        logo: ""
    }
];

function MenuNavbar() {
    const [isLoggedIn, setIsLoggedIn] = useState(
        localStorage.getItem("isLoggedIn") === "true"
    );


    const [cartItems, setCartItems] = useState([]);

    useEffect(() => {
        const handleStorageChange = () => {
            setCartItems(JSON.parse(localStorage.getItem('cartItems')) || []);
        };

        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    const [username, setUsername] = useState(localStorage.getItem("username"));

    const handleLogout = () => {
        localStorage.removeItem("isLoggedIn");
        localStorage.removeItem("username");
        localStorage.removeItem("cartItems");
        setIsLoggedIn(false);
        setUsername(null);
        window.location = "/";
    };

    const handleLoginSuccess = (loginData) => {
        setUsername(loginData.username);
        setIsLoggedIn(true);
        document.querySelector("#modal_entrar > div > a > button").click();
    };

    const getCartItemsCounter = () => {
        let cartItems = JSON.parse(localStorage.getItem("cartItems") ?? "[]");
        let counter = 0;
        cartItems.forEach((cartItem) => {
            counter = counter + cartItem.quantity;
        });
        return counter == null ? 0 : counter;
    }

    const getCartItems = () => {
        return JSON.parse(localStorage.getItem("cartItems" ?? "[]"));
    }

    const siteData = JSON.parse(sessionStorage.getItem('siteData') || `{}`);

    return (
       <div>
           {/* navbar desktop */ }
           <div className='text-white hidden md:block'>
               <BrandingLine/>
               <div className="navbar">
                   <div className="navbar-start">
                       <div className="logo-container">
                           <Link to="/">
                               <img className="logo" src={siteData && siteData.logoImage} alt="Logo"/>
                           </Link>
                       </div>
                   </div>
                   <div className="navbar-center lg:flex">

                       <ul className="menu menu-horizontal px-1">
                           {
                               (!isLoggedIn ? itens.filter(item => !item.auth) : itens).map((item, index) => (
                                   <li key={index}>
                                       <Link to={item.ref}>{item.logo} {item.nome} </Link>
                                   </li>
                               ))}
                       </ul>

                   </div>




                   <div className="navbar-end lg:flex lg:items-center">

                       <LanguageSwitcher />

                       <div className="dropdown dropdown-end ">


                           <label tabIndex={0} className="btn btn-ghost rounded-btn">

                               <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                   <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 10.5V6a3.75 3.75 0 10-7.5 0v4.5m11.356-1.993l1.263 12c.07.665-.45 1.243-1.119 1.243H4.25a1.125 1.125 0 01-1.12-1.243l1.264-12A1.125 1.125 0 015.513 7.5h12.974c.576 0 1.059.435 1.119 1.007zM8.625 10.5a.375.375 0 11-.75 0 .375.375 0 01.75 0zm7.5 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z" />
                               </svg> &nbsp;&nbsp;
                               {getCartItemsCounter()}&nbsp;
                           </label>



                           <ul className="p-2 shadow menu dropdown-content z-[1] bg-base-100 rounded-box w-46 mt-4 ">

                               {
                                   getCartItems() != null && getCartItems().length > 0 ? (
                                       getCartItems() .map((item, index) => (
                                           <li key={index} className="w-full">
                                               <a className="w-full h-2 text-sm menu-cart-font text-left">
                                                   {index + 1} - {item.title.length > 8 ? `${item.title.slice(0, 8)}...` : item.title} ({item.quantity})
                                               </a>
                                           </li>
                                       ))
                                   ) : (
                                       <li className="w-full">
                                           <a className="text-sm w-full menu-cart-font">{i18n.t('site.components.MenuNavbar.cart.empty')}</a>
                                       </li>
                                   )
                               }


                               <li className="w-full bg-base-200 mt-4">
                                   <Link to="/checkout" className="text-sm w-full ">

                                       <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                           <path strokeLinecap="round" strokeLinejoin="round" d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 00-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 00-16.536-1.84M7.5 14.25L5.106 5.272M6 20.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm12.75 0a.75.75 0 11-1.5 0 .75.75 0 011.5 0z" />
                                       </svg>


                                       <span className="text-blue-400 menu-cart-font">{i18n.t('site.components.MenuNavbar.cart.toCart')}</span>
                                   </Link>
                               </li>
                           </ul>
                       </div>



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

                                   <ul
                                       tabIndex={1}
                                       className="menu dropdown-content z-[1] p-2 shadow bg-base-100 rounded-box w-52 mt-4"
                                   >

                                       <li>
                                           <Link className="w-full" to="/panel">
                                               <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                    strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                                   <path strokeLinecap="round" strokeLinejoin="round"
                                                         d="M17.982 18.725A7.488 7.488 0 0012 15.75a7.488 7.488 0 00-5.982 2.975m11.963 0a9 9 0 10-11.963 0m11.963 0A8.966 8.966 0 0112 21a8.966 8.966 0 01-5.982-2.275M15 9.75a3 3 0 11-6 0 3 3 0 016 0z"/>
                                               </svg>
                                               {i18n.t('site.components.MenuNavbar.panel.content')}
                                           </Link>
                                       </li>
                                       <li>
                                           <a className="w-full" onClick={handleLogout}>
                                               <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                    strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                                   <path strokeLinecap="round" strokeLinejoin="round"
                                                         d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15M12 9l-3 3m0 0l3 3m-3-3h12.75"/>
                                               </svg>

                                               {i18n.t('site.components.MenuNavbar.panel.logout')}
                                           </a>
                                       </li>
                                   </ul>

                               </div>
                           </div>
                       ) : (
                           <a href="#modal_entrar" id="entrar"
                              className="bg-red-500 hover:bg-orange-700 text-white font-bold py-2 px-4 rounded ml-4">
                               {i18n.t('site.components.MenuNavbar.panel.login')}
                           </a>
                       )}
                   </div>


                   <div className="modal" id="modal_entrar">
                       <ModalLogin onLoginSuccess={handleLoginSuccess}/>
                   </div>


               </div>
           </div>

           {/* navbar mobile */}
           <div className='text-white md:hidden'>
               <BrandingLine/>
               <div className="navbar bg-base-100">
                   <div className="flex-none">
                       <button className="btn btn-square btn-ghost">
                           <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-5 h-5 stroke-current"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path></svg>
                       </button>
                   </div>
                   <div className="flex-none text-center text-2xl text-gray-200" onClick={() => { window.location.href= "/"}}>
                       {siteData.serverFantasyName}
                   </div>
                   <div className="flex-none">
                       {isLoggedIn ? (
                           <div className="w-100">
                               <div className="dropdown dropdown-end">

                                   <label tabIndex={1} className="btn btn-ghost rounded-btn">

                                       <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                            strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                           <path strokeLinecap="round" strokeLinejoin="round"
                                                 d="M17.982 18.725A7.488 7.488 0 0012 15.75a7.488 7.488 0 00-5.982 2.975m11.963 0a9 9 0 10-11.963 0m11.963 0A8.966 8.966 0 0112 21a8.966 8.966 0 01-5.982-2.275M15 9.75a3 3 0 11-6 0 3 3 0 016 0z"/>
                                       </svg>


                                   </label>

                                   <ul
                                       tabIndex={1}
                                       className="menu dropdown-content z-[1] p-2 shadow bg-base-100 rounded-box w-52 mt-4"
                                   >

                                       <li>
                                           <Link className="text-sm md:text-xl" to="/painel">
                                               <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                    strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                                   <path strokeLinecap="round" strokeLinejoin="round"
                                                         d="M17.982 18.725A7.488 7.488 0 0012 15.75a7.488 7.488 0 00-5.982 2.975m11.963 0a9 9 0 10-11.963 0m11.963 0A8.966 8.966 0 0112 21a8.966 8.966 0 01-5.982-2.275M15 9.75a3 3 0 11-6 0 3 3 0 016 0z"/>
                                               </svg>
                                               <p className='text-sm'>{i18n.t('site.components.MenuNavbar.panel.content')}</p>
                                           </Link>

                                       </li>
                                       <li className='text-sm md:text-xl'>
                                           <a className="w-full" onClick={handleLogout}>
                                               <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                    strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                                   <path strokeLinecap="round" strokeLinejoin="round"
                                                         d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15M12 9l-3 3m0 0l3 3m-3-3h12.75"/>
                                               </svg>
                                               <p className='text-sm'>{i18n.t('site.components.MenuNavbar.panel.logout')}</p>
                                           </a>
                                       </li>
                                   </ul>

                               </div>
                           </div>
                       ) : (
                           <p href="#modal_entrar" id="entrar"
                              className="text-xl btn bg-red-800 text-white">
                               {i18n.t('site.components.MenuNavbar.panel.login')}
                           </p>
                       )}
                   </div>
               </div>
           </div>
       </div>
    );
}

export default MenuNavbar;
