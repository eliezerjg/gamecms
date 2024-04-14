import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Card from '../Card/Card';
import Pagination from "../Pagination/Pagination.jsx";
import Swal from "sweetalert2";
import configurationSingleton from "../../../middlewares/Configurations.jsx";
import i18n from "../../../lib/i18n.jsx"
import Loading from "../Loading/Loading.jsx";
import ErrorLoading from "../ErrorLoading/ErrorLoading.jsx";


function NewsCards() {
    const [items, setItems] = useState([]);
    const [tipo, setTipo] = useState('NEWS');
    const [pageable, setPageable] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem('jwt');
        fetchNews(tipo, 0);
    }, [tipo]);

    const [isLoading, setIsLoading] = useState(true);
    const [isError, setIsError] = useState(false);

    const fetchNews = (tipo, page) => {
        const token = localStorage.getItem('jwt');
        axios.get(`${configurationSingleton.getBaseUrl()}/noticias/tipo/${tipo}?page=${page}&pageSize=6`, {
            headers: {
                Authorization: `Bearer ${token}`,
            }
        })
            .then((response) => {
                const responseData = Array.isArray(response.data.content) ? response.data.content : [response.data.content];
                setItems(responseData);
                setPageable(response.data);
                setIsLoading(false);
            })
            .catch((error) => {
                setIsError(true);
                setIsLoading(false);
                Swal.fire({
                    icon: 'error',
                    title: i18n.t('site.components.NewsCards.fetchNews.title'),
                    text: i18n.t('site.components.NewsCards.fetchNews.text') + tipo.toLowerCase() + '.'
                })
            });
    };

    const updateNews = (newTipo) => {
        setTipo(newTipo);
    };

    const handlePageChange = (page) => {
        fetchNews(tipo, page);
    };

    return (
        <div>

            <div className="flex flex-col items-center mt-6">

                <label className='text-xl mr-12 py-3'>{i18n.t('site.components.NewsCards.choose')}</label>

                <div id="news-types">

                    <button className="btn btn-primary mr-3" onClick={() => updateNews("NEWS")}>{i18n.t('site.components.NewsCards.types.news')}</button>
                    <button className="btn btn-info mr-3" onClick={() => updateNews("EVENTS")}>{i18n.t('site.components.NewsCards.types.event')}</button>
                    <button className="btn btn-warning mr-3" onClick={() => updateNews("MAINTENANCES")}>{i18n.t('site.components.NewsCards.types.maintenance')}</button>
                </div>
            </div>

            <div className="h-96">


                {isLoading ? (
                    <Loading/>
                ) : isError ? (
                    <ErrorLoading  />
                ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-12 m-8 h-full overflow-y-auto">
                    {
                        items.map((item) => (
                            <Card item={item} key={item.id} />
                        ))
                    }

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

export default NewsCards;
