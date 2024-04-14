import "./styles.css";
import React, {useEffect, useState} from 'react';
import axios from 'axios';
import ShopCard from "../ShopCard/ShopCard.jsx";
import Pagination from "../Pagination/Pagination.jsx";
import Swal from "sweetalert2";
import configurationSingleton from "../../../middlewares/Configurations.jsx";
import i18n from "../../../lib/i18n.jsx";
import Loading from "../Loading/Loading.jsx";
import ErrorLoading from "../ErrorLoading/ErrorLoading.jsx";

function ShopCards({onAddToCart}) {
    const [items, setItems] = useState([]);
    const [filteredItems, setFilteredItems] = useState([]);
    const [pageable, setPageable] = useState([]);

    const [isLoading, setIsLoading] = useState(true);
    const [isError, setIsError] = useState(false);

    const fetchShopCards = (page) => {
        axios.get(`${configurationSingleton.getBaseUrl()}/eshop?page=${page}&pageSize=8`)
            .then((response) => {
                const responseData = Array.isArray(response.data.content) ? response.data.content : [response.data.content];
                setItems(responseData);
                setPageable(response.data);
                setIsLoading(false);
            })
            .catch((error) => {
                setIsError(true);
                setIsLoading(false);
                console.log(error);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: i18n.t('site.components.ShopCards.fetchShopCards.errorText')
                });
            });
    };
    useEffect(() => {
        fetchShopCards(0);
    }, []);

    const handlePageChange = (page) => {
        fetchShopCards(page || 0);
    };


    const filterBySearch = (searchTerm) => {
        const filteredItems = items.filter(item =>
            item.title.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setFilteredItems(filteredItems);
    };

    const handleAddToCard = (productData) => {
        onAddToCart(productData);
    };


    return (
        <div className='mb-[20vh] w-full'>



            <div className=' ml-16 mr-16'>
                <h1 className='text-5xl mb-8 mt-6 font-bold'>{i18n.t('site.components.ShopCards.content')} </h1>

                <div className='mb-4 w-full'>
                    <input
                        type='text'
                        className='w-full p-4 border rounded focus:ring focus:ring-blue-300'
                        placeholder={i18n.t('site.components.ShopCards.searchPlaceHolder')}
                        onChange={(e) => filterBySearch(e.target.value)}
                    />
                </div>

                {isLoading ? (
                    <Loading/>
                ) : isError ? (
                    <ErrorLoading  />
                ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 m-2">
                    {(filteredItems.length > 0 ? filteredItems : items).map((item) => (
                        <ShopCard key={item.id} item={item} onAddToCart={() => handleAddToCard(item)}/>
                    ))}
                </div>
                )}


            </div>


            <Pagination
                currentPage={pageable.number}
                nextPage={pageable.last ? pageable.number : pageable.number + 1}
                previousPage={pageable.first ? pageable.number : pageable.number - 1}
                totalPages={pageable.totalPages}
                onPageChange={(page) => handlePageChange(page)}
            />

        </div>
    );
}

export default ShopCards;
