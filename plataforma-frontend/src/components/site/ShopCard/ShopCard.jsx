import React from 'react';
import {Link} from 'react-router-dom';
import Swal from "sweetalert2";
import {ToastContainer, toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import i18n from "../../../lib/i18n.jsx";
import Configurations from "../../../middlewares/Configurations.jsx";

function ShopCard({item, onAddToCart}) {
    const formatDate = (date) => {
        const options = {day: '2-digit', month: '2-digit', year: 'numeric'};
        return new Date(date).toLocaleDateString('pt-BR', options);
    };

    const addToCart = (item) => {
        toast.success(`[ ${item.title} ]  Adicionado ao carrinho`, {
                position: "bottom-right",
                autoClose: 1500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
                onClick: () => { window.location.href = "/checkout"; }
            });

        let itens = JSON.parse(localStorage.getItem('cartItems') ?? "[]") ?? [];

        let catchedProduct = itens.filter(n => n.id === item.id);
        if (catchedProduct.length > 0) {
            catchedProduct[0].quantity++;
            itens[itens.indexOf(catchedProduct[0])] = catchedProduct[0];
        }

        if (itens.length === 0 || catchedProduct.length === 0) {
            item.quantity = 1;
            itens.push(item);
        }


        localStorage.setItem('cartItems', JSON.stringify(itens));
        onAddToCart(item);

    }

    const doCheckout = (item) => {
        addToCart(item);
        window.location.href = "/checkout";
    };


    const moreInfo = (item) => {
        Swal.fire(
            i18n.t('site.components.ShopCard.moreInfo.details'),
            '<div class="text-justify ml-4 mb-6">' +
            '<strong>'+i18n.t('site.components.ShopCard.moreInfo.item')+'</strong> ' + item.title + '<BR> ' +
            '<strong>'+i18n.t('site.components.ShopCard.moreInfo.description')+'</strong> ' + item.description
            + '</div>'
            ,


            'info'
        );
    };

    return (
        <div className="rounded shadow">

            <ToastContainer/>
            <div className="card card-compact w-94 bg-base-100 shadow-xl">
                <div className="flex justify-end">
                    <div className="absolute top-4 right-4">
                        <button onClick={() => moreInfo(item)}>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5"
                                 stroke="currentColor" className="w-8 h-8">
                                <path strokeLinecap="round" strokeLinejoin="round"
                                      d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z"/>
                            </svg>
                        </button>
                    </div>
                </div>
                <figure className="px-2 pt-2">
                    <img style={{ width: '240px', height: '240px' }} src={item.image}  alt={item.title}
                         onClick={() => moreInfo(item)}
                    /></figure>
                <div className="card-body">

                    <h2 className="card-title text-2xl font-semibold text-white">{item.title}</h2>
                    <p className="text-gray-500"
                       dangerouslySetInnerHTML={{__html: item.description.length > 30 ? `${item.description.substring(0, 30)}...` : item.description}}></p>
                    <p className="text-white text-xl">{i18n.t('site.components.ShopCard.value')} {Configurations.getCurrency()["sign"]} {item.value}</p>

                    <div className="">


                        <button className='btn btn-info  w-2/4 text-right float-left' onClick={() => doCheckout(item)}>

                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5"
                                 stroke="currentColor" className="w-6 h-6">
                                <path strokeLinecap="round" strokeLinejoin="round"
                                      d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 00-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 00-16.536-1.84M7.5 14.25L5.106 5.272M6 20.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm12.75 0a.75.75 0 11-1.5 0 .75.75 0 011.5 0z"/>
                            </svg>
                            &nbsp;

                            <p className='text-lg text-center'>{i18n.t('site.components.ShopCard.buy')}</p>
                        </button>

                        <button className='btn btn-success btn-outline text-2x1 float-right' onClick={() => addToCart(item)}>
                            <p className='text-2xl text-center'>+</p>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5"
                                 stroke="currentColor" className="w-6 h-6">
                                <path strokeLinecap="round" strokeLinejoin="round"
                                      d="M15.75 10.5V6a3.75 3.75 0 10-7.5 0v4.5m11.356-1.993l1.263 12c.07.665-.45 1.243-1.119 1.243H4.25a1.125 1.125 0 01-1.12-1.243l1.264-12A1.125 1.125 0 015.513 7.5h12.974c.576 0 1.059.435 1.119 1.007zM8.625 10.5a.375.375 0 11-.75 0 .375.375 0 01.75 0zm7.5 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z"/>
                            </svg>


                        </button>


                    </div>
                </div>
            </div>
        </div>
    );
}

export default ShopCard;
