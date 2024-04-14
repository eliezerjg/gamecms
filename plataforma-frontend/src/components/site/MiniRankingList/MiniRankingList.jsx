import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Configurations from "../../../middlewares/Configurations.jsx";
import i18n from "../../../lib/i18n.jsx";
import Loading from "../Loading/Loading.jsx";
import ErrorLoading from "../ErrorLoading/ErrorLoading.jsx";

const MiniRankingList = () => {
    const initialTableData = {
        head: {
            position: i18n.t('site.components.MiniRankingList.head.position'),
            class: i18n.t('site.components.MiniRankingList.head.class'),
            name: i18n.t('site.components.MiniRankingList.head.name'),
            guildflag: i18n.t('site.components.MiniRankingList.head.guildflag'),
            pkpoints: i18n.t('site.components.MiniRankingList.head.pkpoints'),
            status: i18n.t('site.components.MiniRankingList.head.status')
        },
        rows: [],
    };

    const [isLoading, setIsLoading] = useState(true);
    const [isError, setIsError] = useState(false);

    const [tableData, setTableData] = useState(initialTableData);

    const fetchData = async () => {
        try {
            const response = await axios.get(Configurations.getIntegracaoUrl() + '/ranking/miniranking', { timeout: 10000 });
            const data = response.data;
            setTableData((prevData) => ({ ...prevData, rows: data.rankingData }));
            setIsLoading(false);
        } catch (error) {
            setIsError(true);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    const classNameToFor = {
        "0": <img className='mx-auto' src='/assets/ranking/tk-class.png' width='15' height='15' />,
        "1": <img className='mx-auto' src='/assets/ranking/fm-class.png' width='15' height='15' />,
        "2": <img className='mx-auto' src='/assets/ranking/bm-class.png' width='15' height='15' />,
        "3": <img className='mx-auto' src='/assets/ranking/ht-class.png' width='15' height='15' />
    };

    const evolutionNameToFor = {
        "1": "MORTAL",
        "2": "ARCH",
        "3": "CELESTIAL",
        "4": "CELESTIAL CS",
        "5": "S. CELESTIAL",
        "6": "HARDCORE",
        "7": "HARDCORE A.",
        "8": "HARDCORE CS.",
        "9": "S. HARDCORE",
    };

    const kingdomNameToFor = {
        "0": <img className='mx-auto' src='/assets/ranking/white-cape.png' width='15' height='15' />,
        "7": <img className='mx-auto' src='/assets/ranking/blue-cape.png' width='15' height='15'/>,
        "8": <img className='mx-auto' src='/assets/ranking/red-cape.png' width='15' height='15'/>,

    };

    const getGuildFlag = (guildId) => {
        if(guildId === 0){
            return "( ? )";
        }

        return    <img className='w-6 mx-auto' src={`${Configurations.getBaseUrl()}/guild/guildmark/w01000${guildId}.bmp`} />;
    }

    const getOnlineStatus = (nick) => {
        const status =  tableData.onlines[nick];
        return <button className={ status === "0" ? "bg-red-800 text-white p-2 rounded-sm" : "bg-green-600 text-white p-2"}> { status === "0" ? "Off" : "On"}</button>;
    }



    return (
        <div className='float-right mt-6'>
            {isLoading ? (
                <Loading/>
            ) : isError ? (
                <ErrorLoading  />
            ) : (
                <div>
                    <div className='w-full'>
                        <div className="float-left text-xl text-white">{i18n.t('site.components.MiniRankingList.content')}</div>
                        <a href='/ranking'><button className='float-right btn bg-red-800 btn-sm mb-2 text-white'>{i18n.t('site.components.MiniRankingList.readAll')}</button></a>
                    </div>
                    <table className="table table-zebra table-compact text-center">
                        <thead>
                        <tr className='text-center'>
                            {Object.values(tableData.head).map((value, index) => (
                                <th key={index} className='text-center'>{value}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {tableData.rows.map((row, index) => (
                            <tr className='text-center' key={index} style={{ height: '8px' }}>
                                <td>{(index + 1 <= 3) ? <img className='mx-auto' width="15" src={'/assets/ranking/top' + (index + 1) + '.webp'} /> : (index + 1)}</td>
                                <td className=''>{classNameToFor[row['className']]}</td>
                                <td> {row['nick']}</td>
                                <td> {getGuildFlag(row['guild_id'])}</td>
                                <td> {row['frags']}</td>
                                <td> {getOnlineStatus(row['nick'])}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default MiniRankingList;
