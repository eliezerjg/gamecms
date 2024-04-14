import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import "./styles.css";
import {format} from 'date-fns';
import Loading from "../Loading/Loading.jsx";
import configurationSingleton from "../../../middlewares/Configurations.jsx";
import i18n from "../../../lib/i18n.jsx";



function PostContent({postId}) {
    const [postData, setPostData] = useState(null);

    const fetchPostData = async () => {
        const token = localStorage.getItem('jwt');

        try {
            const response = await axios.get(`${configurationSingleton.getBaseUrl()}/noticias/${postId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            setPostData(response.data);
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: i18n.t('site.components.PostContent.fetchPostData.error'),
                text: i18n.t('site.components.PostContent.fetchPostData.errorText') + ' Status:' + error.response.status + ' ' + error.response.data.message + '.'
            })
        }
    };

    useEffect(() => {
        fetchPostData();
    }, [postId]);

    return (
        <div className='bg-gray-950 w-90 ml-2 mr-2 py-12 w-full h-[70vh] overflow-y-auto'>
            {postData ? (
                <div className='container mx-auto text-center mb-64'>
                    <strong className='text-center text-white text-3xl  p-4'>{i18n.t('site.components.PostContent.title')} {postData.title} -
                        #{postId}</strong>

                    <div
                        className='text-justify text-white py-4 m-4'
                        dangerouslySetInnerHTML={{__html: `${postData.description}`}}
                    ></div>

                    <div
                        className='text-justify text-white py-4 m-4'
                        dangerouslySetInnerHTML={{__html: `${postData.unformatedHtmlDescription}`}}
                    ></div>

                    <p className='text-justify text-white py-4 m-4'>{i18n.t('site.components.PostContent.authoredBy')} {postData.author},
                        em: {format(new Date(postData.data), 'dd/MM/yyyy')}.</p>
                </div>
            ) : (
                <Loading/>
            )}
        </div>
    );
}

export default PostContent;
