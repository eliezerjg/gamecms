import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Configurations from "../../../middlewares/Configurations.jsx";
import i18n from "../../../lib/i18n.jsx";
import Loading from "../Loading/Loading.jsx";
import ErrorLoading from "../ErrorLoading/ErrorLoading.jsx";

const RankingList = () => {
    const initialTableData = {
        head: {
            position: i18n.t('site.components.RankingList.head.position'),
            class: i18n.t('site.components.RankingList.head.class'),
            name: i18n.t('site.components.RankingList.head.name'),
            guildflag: i18n.t('site.components.RankingList.head.guildflag'),
            pkpoints: i18n.t('site.components.RankingList.head.pkpoints'),
            arenakills : i18n.t('site.components.RankingList.head.arenakills'),
            arenawins : i18n.t('site.components.RankingList.head.arenawins'),
            level: i18n.t('site.components.RankingList.head.level'),
            evolution: i18n.t('site.components.RankingList.head.evolution'),
            kingdom: i18n.t('site.components.RankingList.head.kingdom'),
            status: i18n.t('site.components.RankingList.head.status')
        },
        rows: [],
    };

    const [isLoading, setIsLoading] = useState(true);
    const [isError, setIsError] = useState(false);
    const [tableData, setTableData] = useState(initialTableData);

    const fetchRankingData = async () => {
        try {
            const response = await axios.get(Configurations.getIntegracaoUrl() + '/ranking', { timeout: 10000 });
            const data = response.data;
            setTableData((prevData) => ({ ...prevData, rows: data.rankingData }));
            setIsLoading(false);
        } catch (error) {
            setIsError(true);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchRankingData();
    }, []);

    const maxImageSizes = 15;
    const classNameToFor = {
        "0": <img className='mx-auto' src='/assets/ranking/tk-class.png' width={maxImageSizes} height={maxImageSizes} />,
        "1": <img className='mx-auto' src='/assets/ranking/fm-class.png' width={maxImageSizes} height={maxImageSizes} />,
        "2": <img className='mx-auto' src='/assets/ranking/bm-class.png' width={maxImageSizes} height={maxImageSizes} />,
        "3": <img className='mx-auto' src='/assets/ranking/ht-class.png' width={maxImageSizes} height={maxImageSizes} />
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
        "0": <img className='mx-auto' src='/assets/ranking/white-cape.png' width={maxImageSizes} height={maxImageSizes} />,
        "7": <img className='mx-auto' src='/assets/ranking/blue-cape.png' width={maxImageSizes} height={maxImageSizes}/>,
        "8": <img className='mx-auto' src='/assets/ranking/red-cape.png' width={maxImageSizes} height={maxImageSizes}/>,

    };

    const getGuildFlag = (guildId) => {
        if(guildId === 0){
            return "( ? )";
        }

        return    <img className='mx-auto' width={maxImageSizes} src={`${Configurations.getBaseUrl()}/guild/guildmark/w01000${guildId}.bmp`} />;
    }

    const getOnlineStatus = (nick) => {
        const status =  tableData.onlines[nick];
        return <button className={ status === "0" ? "bg-red-800 text-white p-2 rounded-sm" : "bg-green-600 text-white p-2"}> { status === "0" ? "Off" : "On"}</button>;
    }



    return (
        <div className='mb-12 '>
            <div className="mb-6 homepagetitle text-center text-white">{i18n.t('site.components.RankingList.content')}</div>
            <div>
                {isLoading ? (
                    <Loading/>
                ) : isError ? (
                    <ErrorLoading  />
                ) : (
                <table className="table table-zebra table-compact mx-auto text-center mb-24  ">
                    <thead className='text-center'>
                    <tr className='text-center'>
                        {Object.values(tableData.head).map((value, index) => (
                            <th key={index} className='text-center'>{value}</th>
                        ))}
                    </tr>
                    </thead>
                    <tbody className='text-center'>
                    {tableData.rows.map((row, index) => (
                        <tr key={index} className='text-center'>
                            <td>{(index + 1 <= 3) ? <img className='mx-auto' width={maxImageSizes} src={'/assets/ranking/top' + (index + 1) + '.webp'} /> : (index + 1) }</td>
                            <td className=''> {classNameToFor[ row['className'] ]}</td>
                            <td> {row['nick']}</td>
                            <td> {getGuildFlag(row['guild_id'])}</td>
                            <td> {row['frags']}</td>
                            <td> {row['arenaKill']}</td>
                            <td> {row['arenaWin']}</td>
                            <td> {row['celestialLevel'] !== "0" && row['subCelestialLevel'] !== 0 ? row['subCelestialLevel'] + " / " + row['celestialLevel'] : row['level'] } </td>
                            <td> {evolutionNameToFor[ row['evolution'] ]}</td>
                            <td> {kingdomNameToFor[ row['kingdom'] ]}</td>
                            <td> { getOnlineStatus(row['nick']) }</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                )}
            </div>
        </div>
    );
};

export default RankingList;
