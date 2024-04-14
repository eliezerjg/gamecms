import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Panel from './routes/Site/Panel.jsx';
import Ranking from './routes/Site/Ranking';
import Shop from './routes/Site/Shop';
import News from './routes/Site/News';
import Post from './routes/Site/Post.jsx';
import './index.css';
import Register from './routes/Site/Register.jsx';
import Download from "./routes/Site/Download.jsx";
import Checkout from "./routes/Site/Checkout.jsx";
import Home from "./routes/Site/Home.jsx";
import HomeAdmin from "./routes/Admin/Home.jsx";
import Overview from "./routes/Admin/Tabs/Overview.jsx";
import NewsManagement from "./routes/Admin/Tabs/NewsManagement.jsx";
import GuildmarksManagement from "./routes/Admin/Tabs/GuildmarksManagement.jsx";
import PurchasesHistoryManagement from "./routes/Admin/Tabs/PurchasesHistoryManagement.jsx";
import AccountSettingsManagement from "./routes/Admin/Tabs/AccountSettingsManagement.jsx";
import axios from "axios";
import configurationSingleton from "./middlewares/Configurations.jsx";
import Configurations from "./middlewares/Configurations.jsx";
import ShopManagement from "./routes/Admin/Tabs/ShopManagement.jsx";
import Swal from "sweetalert2";
import './lib/i18n.jsx'

const router = createBrowserRouter(
    [
        {
            Element: <App/>,
            children: [
                {
                    path: "/",
                    element: <Home/>
                },
                {
                    path: "/panel",
                    element: <Panel/>
                },
                {
                    path: "/ranking",
                    element: <Ranking/>
                },
                {
                    path: "/shop",
                    element: <Shop/>
                },
                {
                    path: "/news",
                    element: <News/>
                },
                {
                    path: "/post/:postId",
                    element: <Post/>
                },
                {
                    path: "/register",
                    element: <Register/>
                },
                {
                    path: "/download",
                    element: <Download/>
                },
                {
                    path: "/ranking",
                    element: <Ranking/>
                },
                {
                    path: "/checkout",
                    element: <Checkout/>
                },

                // admin routes
                {
                    path: "/admin/", // root
                    element: <HomeAdmin/>
                },
                {
                    path: "/admin/panel",
                    element: <Overview/>
                },
                {
                    path: "/admin/overview",
                    element: <Overview/>
                },
                {
                    path: "/admin/news",
                    element: <NewsManagement/>
                },
                {
                    path: "/admin/guildmarks",
                    element: <GuildmarksManagement/>
                },
                {
                    path: "/admin/purchases",
                    element: <PurchasesHistoryManagement/>
                },
                {
                    path: "/admin/shop",
                    element: <ShopManagement/>
                },
                {
                    path: "/admin/settings",
                    element: <AccountSettingsManagement/>
                }

            ]
        }
    ]
);


const siteData = JSON.parse(sessionStorage.getItem('siteData'));

const doConfigurationsLoading = () => {
    const fetchData = async () => {
        const isStoredDate = sessionStorage.getItem('lastDateRefresh') !== null;
        const storedDate = isStoredDate ? new Date(JSON.parse(sessionStorage.getItem('lastDateRefresh'))) : null;


        console.log(siteData);

        if (siteData === null || storedDate && (new Date() - storedDate) >= 2 * 60 * 1000) {
            try {
                const response = await axios.get(`${configurationSingleton.getBaseUrl()}/public/configuracoes`);
                let dados = response.data;
                document.title = dados.serverFantasyName;
                document.getElementById("favicon").href = dados.favIconImage;
                document.body.style.backgroundImage = 'url(' + dados.backgroundImage + ')';
                sessionStorage.setItem('siteData', JSON.stringify(response.data));
                sessionStorage.setItem('lastDateRefresh', JSON.stringify(new Date()));
                window.location.reload();
            } catch (error) {
                console.log(error);
            }
        }else{
            document.title = siteData.serverFantasyName;
            document.getElementById("favicon").href = siteData.favIconImage;
            document.body.style.backgroundImage = 'url(' + siteData.backgroundImage + ')';
        }
    };


        fetchData();

}
const doInitializeLaunchMode = () => {
    const releaseDateInLong = new Date(siteData.releaseDate).getTime();
    let remainingTime = releaseDateInLong - new Date().getTime();
    let timerInterval;

    if (siteData && siteData.enableReleaseDateCounter && !window.location.href.toString().includes("admin") && remainingTime > 0) {
        Swal.fire({
            title: "<p class='text-white'>Prepare-se! " + siteData.serverFantasyName + " "+ siteData.releaseMessage + "</p>",
            html: `
                <div id="custom-swal-content">
                    <p class="text-white">Você será liberado em: <b></b>. Enquanto isso:</p>
                    <a href="${siteData.whatsappUrl}" target="_blank">
                        <button class="btn bg-green-600 text-white mt-4">Entrar no Whatsapp</button>
                    </a>
                    <a href="${siteData.discordUrl}" target="_blank">
                        <button class="btn btn-primary text-white mt-4">Entrar no Discord</button>
                    </a>
                </div>
            `,
            allowOutsideClick: false,
            showCloseButton: false,
            timer: remainingTime,
            timerProgressBar: true,
            didOpen: () => {
                Swal.showLoading();
                const timer = Swal.getPopup().querySelector("b");
                const customContent = Swal.getPopup().querySelector("#custom-swal-content");

                timerInterval = setInterval(() => {
                    const currentTime = new Date().getTime();
                    remainingTime = releaseDateInLong - currentTime;

                    if (remainingTime > 0) {
                        const days = Math.floor(remainingTime / (1000 * 60 * 60 * 24));
                        const hours = Math.floor((remainingTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                        const minutes = Math.floor((remainingTime % (1000 * 60 * 60)) / (1000 * 60));
                        const seconds = Math.floor((remainingTime % (1000 * 60)) / 1000);

                        timer.innerHTML = `${days}d ${hours}h ${minutes}m ${seconds}s`;
                    } else {
                        timer.innerHTML = '0d 0h 0m 0s';
                        clearInterval(timerInterval);
                    }

                    Swal.getPopup().style.background = 'url(' + siteData.launcherImage + ')';
                    Swal.getPopup().style.backgroundSize = 'cover';
                    Swal.getPopup().style.backgroundRepeat = 'no-repeat';
                    Swal.getPopup().style.border = '2px solid #7a0006';
                    Swal.getPopup().style.boxShadow = '0px 0px 10px rgba(0, 0, 0, 0.5)';
                }, 1000);
            },
            willClose: () => {
                clearInterval(timerInterval);
            },
            width: '94%',
        }).then((result) => {
            if (result.dismiss === Swal.DismissReason.timer) {
                console.log("Fui fechado pelo temporizador");
            }
        });
    }

};
const doBlockWhenPlatformIsNotActive = () => {
    if(!siteData.isActive && Configurations.profile === Configurations.getProfiles().production){
        document.body.innerHTML = '<p class="text-white text-3xl bg-red-600">Plataforma suspensa, conforme o estipulado em 7 dias após a suspensão, a mesma será deletada. <a class="text-green-600" href="https://wa.me/351926525637?&text=Gostaria de quitar minhas dividas, sou:'+window.location.href+'"> Clique aqui e Fale Conosco.</a>, evite custos adicionais de reinstalação.</p>';
    }
};


const initFrontEnd = () =>  {
    doConfigurationsLoading();
    doInitializeLaunchMode();
    doBlockWhenPlatformIsNotActive();
};

initFrontEnd();


ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>,
);
