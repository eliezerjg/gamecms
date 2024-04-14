import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import "./styles.css";
import { format } from 'date-fns';
import Loading from "../Loading/Loading.jsx";
import configurationSingleton from "../../../middlewares/Configurations.jsx"
import i18n from "../../../lib/i18n.jsx";

function DownloadContent() {
  const [downloadData, setDownloadData] = useState(null);

  useEffect(() => {
    fetchDownload();
  }, [1]);

  const fetchDownload = async () => {
    const token = localStorage.getItem('jwt');

    try {
      const response = await axios.get(`${configurationSingleton.getBaseUrl()}/noticias/tipo/DOWNLOADS?page=0&pageSize=1`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setDownloadData(response.data.content[0]);
    } catch (error) {
      console.log(error);
      Swal.fire({
        icon: 'error',
        title: i18n.t('site.components.DownloadContent.fetchDownload.title'),
        text: i18n.t('site.components.DownloadContent.fetchDownload.text') + ' Status: ' +error.response.status+ ' '+ error.response.data.message + '.'
      })
    }
  };

  return (
      <div className='bg-gray-950 w-90 ml-2 mr-2 py-12 w-full h-[70vh] overflow-y-auto'>
      {downloadData ? (
          <div className='container mx-auto text-center'>
          <strong className='text-center text-white text-3xl  p-4'>{i18n.t('site.components.DownloadContent.title')} {downloadData.title} </strong>

          <div
              className='text-justify text-white py-4 m-4'
              dangerouslySetInnerHTML={{__html: `${downloadData.description}`}}
          ></div>

          <div
              className='text-justify text-white py-4 m-4'
              dangerouslySetInnerHTML={{__html: `${downloadData.unformatedHtmlDescription}`}}
          ></div>


          <p className='text-justify text-white py-4 m-4'>{i18n.t('site.components.DownloadContent.authoredBy')}: {downloadData.author}, {i18n.t('site.components.DownloadContent.at')}: {format(new Date(downloadData.data), 'dd/MM/yyyy')}.</p>
        </div>
      ) : (
          <div className='container mx-auto text-center h-full text-3xl'>
            {downloadData === undefined ? i18n.t('site.components.DownloadContent.noDownloadsAvailable') :  <Loading/>}
          </div>
      )}
    </div>
  );
}

export default DownloadContent;
