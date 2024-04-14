import React from 'react';
import "./styles.css";
import Configurations from "../../../middlewares/Configurations.jsx";
import i18n from "../../../lib/i18n.jsx"

function CartTable({ itens, onAddToCart }) {

    let cartItems = JSON.parse(localStorage.getItem('cartItems') ?? "[]") ?? [];
    const updateItem = (item, strategy) => {
        let catchedProductIndex = cartItems.findIndex(n => n.id === item.id);

        if (strategy === 'minus' && cartItems[catchedProductIndex].quantity === 1) {
            cartItems = cartItems.filter(n => n !== cartItems[catchedProductIndex]);
        } else if (strategy === 'minus') {
            cartItems[catchedProductIndex].quantity -= 1;
        } else if (strategy === 'sum') {
            cartItems[catchedProductIndex].quantity += 1;
        }

        localStorage.setItem('cartItems', JSON.stringify(cartItems));
        onAddToCart(item);
    };

    return (
        <div className="md:p-8 h-[58vh]">
                <div className="h-full overflow-y-auto overflow-x-auto ">
                    <table className='table table-compact table-zebra w-full' data-theme="dark">
                        <thead >
                            <th className='text-center'>{i18n.t('site.components.CartTable.image')}</th>
                            <th>{i18n.t('site.components.CartTable.item')}</th>
                            <th className='text-right'>{i18n.t('site.components.CartTable.value')} ({Configurations.getCurrency()["sign"]})</th>
                            <th className='text-center'>{i18n.t('site.components.CartTable.quantity')}</th>
                        </thead>
                        <tbody>
                        {itens && itens.length > 0 && itens.map((item) => (
                            <tr key={item.id} className='w-full'>
                                <td >
                                    <img
                                        src={item.image}
                                        className="md:w-10 sm:w-2.5 md:mx-auto"
                                    />
                                </td>

                                <td className='text-justify'>
                                    <strong className="text-white sm:text-sm md:text-sm font-medium mb-1">
                                        {item.title}
                                    </strong>
                                    <p className="text-gray-500 sm:display-none" dangerouslySetInnerHTML={{ __html: item.description.length > 20 ? `${item.description.substring(0, 20)}...` : item.description }}></p>
                                </td>


                                <td className='text-right'>
                                    <p className='text-white md:text-2xl'>{Configurations.getCurrency()["sign"]} {item.value}</p>
                                </td>

                                <td className='text-center'>
                                    <button className="btn btn-outline hover:btn-error mr-2 md:text-xl" onClick={() => updateItem(item, "minus")}>-</button>
                                    <input
                                        className="input mx-auto w-16 text-center md:text-xl"
                                        type="number"
                                        value={(item.quantity !== undefined ? item.quantity : 1)}
                                        disabled={true}
                                    />
                                    <button className="btn btn-outline hover:btn-success  ml-2 md:text-xl" onClick={() => updateItem(item, "sum")}>+</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                <a href="/shop" className="text-xl text-blue-500 hover:text-white">{cartItems.length === 0 ? i18n.t('site.components.CartTable.emptybacktoshop') : ''}</a>
                </div>
        </div>
    );
}

export default CartTable;
