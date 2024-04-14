import React, {useEffect, useState} from 'react';
import axios from "axios";
import configurationSingleton from "../../middlewares/Configurations.jsx";
import Swal from "sweetalert2";
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import BreadCrumbs from "../../components/site/BreadCrumbs/BreadCrumbs.jsx";
import CartTable from "../../components/site/CartTable/CartTable.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";
import i18n from "../../lib/i18n.jsx";
import Configurations from "../../middlewares/Configurations.jsx";

const barraCrumbs = [
    {
        type: "a",
        name: "Home",
        ref: "/"
    },
    {
        type: "a",
        name: "Shop",
        ref: "/shop"
    },
    {
        type: "a",
        name: "Checkout",
        ref: "/checkout"
    }
];

const Checkout = () => {

    const [enablePurchaseButton, setEnablePurchaseButton] = useState(true);

    let items = JSON.parse(localStorage.getItem('cartItems') || "[]");
    let clearCart = () => {
        if (localStorage.getItem('cartItems').length > 0) {
            localStorage.setItem('cartItems', "[]");
        }
        window.location = "/shop";
    };

    const [cartItems, setCartItems] = useState([]);

    const handleAddToCart = (productData) => {
        setCartItems([...cartItems, productData]);
    };

    let total = items.map(n => n.value * n.quantity).reduce((a, b) => {
        return a + b;
    }, 0);

    const choosePaymentMethod = (paymentMethod = 'mercadopago') => {
        createPreferenceAndRedirect(paymentMethod);
    };

    const createPreferenceAndRedirect = async (paymentMethod) => {
        if (localStorage.getItem('username') === null) {
            Swal.fire(
                i18n.t('site.routes.Checkout.createPreferenceAndRedirect.error'),
                i18n.t('site.routes.Checkout.createPreferenceAndRedirect.shouldBeLoggedToProceedWithPurchase'),
                'error'
            ).then(() => {
                document.getElementById("entrar").click();
            });
            return;
        }

        if (items.length === 0) {
            Swal.fire(
                i18n.t('site.routes.Checkout.createPreferenceAndRedirect.error'),
                i18n.t('site.routes.Checkout.createPreferenceAndRedirect.yourCartCantBeEmpty'),
                'error')
            return;
        }

        setEnablePurchaseButton(false);
        const response = await axios.post(
            // TODO: this needs to be adapted by the selected payment method
            `${configurationSingleton.getBaseUrl()}/mercadopago/createPreference`,
            {
                cart: JSON.parse(localStorage.getItem('cartItems') || "[]"),
                username: localStorage.getItem('username')
            }).then((retorno) => {
            // TODO: this needs to be adapted by the selected payment method
            Swal.fire(
                i18n.t('site.routes.Checkout.createPreferenceAndRedirect.warning'),
                i18n.t('site.routes.Checkout.createPreferenceAndRedirect.redirectWarningText'),
                'warning'
            ).then(() => {
                location.href = retorno.data.initPoint;
            });
            setEnablePurchaseButton(true);
        });
    };


    return (
        <div className='h-screen'>
            <MenuNavbar/>
            <BreadCrumbs itensCrumbs={barraCrumbs}/>

            <div className='bg-gray-900 p-2 h-full mb-64'>

                <h1 className="text-center text-3xl font-bold bg-blue-700 text-white">{i18n.t('site.routes.Checkout.content')}</h1>

                <div className="overflow-y-auto h-full">
                    <div className="md:float-left md:w-3/4 sm:w-1/2">
                        <CartTable itens={items} onAddToCart={handleAddToCart}/>
                    </div>
                    <div className="md:float-right md:w-1/4 sm:w-full md:p-2 ">

                        <div>

                            <p className="md:text-3xl text-white md:float-left md:mb-4 sm:float-right mt-2">
                                {/* needs to adapt here adding convertions to other currencies */}
                                <strong>{i18n.t('site.routes.Checkout.total')} {Configurations.getCurrency()["sign"]}: </strong>{total.toLocaleString('pt-BR', {
                                style: 'currency',
                                currency: 'BRL'
                            })}

                            </p>




                            <button className="btn btn-success w-full mb-4 mt-2 text-white md:text-md hover:btn-outline"
                                    onClick={() => choosePaymentMethod()} disabled={!enablePurchaseButton}>
                                {i18n.t('site.routes.Checkout.completeThePayment')}
                            </button>
                        </div>

                        {/* TODO: payment methods flags needs to be adapted by country and by each platform */}
                        <div className='w-full flex'>
                            <img className='flex-1 w-34 h-16 mr-5' src='compra-segura.png' width='160'/>
                            <img className='flex-1 w-40 h-12 mt-2 mr-5' src='site-blindado.png' width='160'/>
                            <img className='flex-1 w-40 h-12 mt-2' src='mercadopago.png' width='160'/>
                        </div>

                    </div>
                </div>






            </div>

            <Footer />

        </div>
    );
};

export default Checkout;
