package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.*;

/**
 * Logic for the TOS page GUI
 *
 * @author Josh Burt
 */

public class TOSController {

    @FXML
    private TextFlow textFlowBox;

public void initialize() {
    //Its a tad large just a creative commons share alike licence
    Text terms = new Text("Licence\n" +
            "THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENCE (\"CCPL\" OR \"LICENCE\"). THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS AUTHORISED UNDER THIS LICENCE OR COPYRIGHT LAW IS PROHIBITED. BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS LICENCE. THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.\n" +
            "This Creative Commons New Zealand Public Licence enables You (all capitalised terms defined below) to view, edit, modify, translate and distribute Works worldwide, under the terms of this licence, provided that You credit the Original Author.\n" +
            "“The Licensor”\n" +
            "and\n" +
            "“You”\n" +
            "agree as follows:\n" +
            "1. Definitions\n" +
            "\n" +
            "    “Adaptation” means any work created by the editing, modification, adaptation or translation of the Work in any media (however a work that constitutes a Collection will not be considered an Adaptation for the purpose of this Licence). For the avoidance of doubt, where the Work is a musical composition or sound recording, the synchronisation of the Work in timed-relation with a moving image (\"synching\") will be considered an Adaptation for the purpose of this Licence.\n" +
            "    “Applicable Licence” means one of the licences described in clause 2.3(a), (b) or (c).\n" +
            "    “Attribution means acknowledging all the parties who have contributed to and have rights in the Work or Collection under this Licence; and “Attribute” has a corresponding meaning.\n" +
            "    “Collection” means the Work in its entirety in unmodified form along with one or more other separate and independent works, assembled into a collective whole.\n" +
            "    “Creative Commons Compatible Licence” means a license that is listed at https://creativecommons.org/compatiblelicenses that has been approved by Creative Commons as being essentially equivalent to this Licence, including, at a minimum, because that licence: (i) contains terms that have the same purpose, meaning and effect as the Licence Elements of this Licence; and, (ii) explicitly permits the relicensing of adaptations of works made available under that licence under this Licence or a Creative Commons jurisdiction license with the same Licence Elements as this Licence.\n" +
            "    “Licence” means this Creative Commons New Zealand Public Licence agreement.\n" +
            "    “Licence Elements” means the following high-level licence attributes indicated in the title of this Licence: Attribution, Share-Alike.\n" +
            "    “Licensor” means one or more legally recognised persons or entities offering the Work under the terms and conditions of this Licence.\n" +
            "    “Original Author” means the individual(s) or entity/ies who created the Work.\n" +
            "    “Work” means the work protected by copyright which is offered under the terms of this Licence.\n" +
            "    “You” means an individual or entity exercising rights under this Licence who has not previously violated the terms of this Licence with respect to the Work, or who has received express permission from the Licensor to exercise rights under this Licence despite a previous violation.\n" +
            "    For the purpose of this Licence, when not inconsistent with the context, words in the singular number include the plural number.\n" +
            "\n" +
            "2. Licence Terms\n" +
            "2.1 Subject to the terms of this agreement the Licensor hereby grants to You a worldwide, royalty-free, non-exclusive, Licence for use and for the duration of copyright in the Work.\n" +
            "You may:\n" +
            "\n" +
            "    copy the Work;\n" +
            "    create one or more Adaptations. You must take reasonable steps to ensure any Adaptation clearly identifies that changes were made to the original Work;\n" +
            "    incorporate the Work into one or more Collections;\n" +
            "    copy Adaptations or the Work as incorporated in any Collection; and\n" +
            "    publish, distribute, archive, perform or otherwise disseminate the Work, the Adaptation or the Work as incorporated in any Collection, to the public.\n" +
            "\n" +
            "All these rights may be exercised in any material form in any media whether now known or hereafter created. All these rights also include the right to make such modifications as are technically necessary to exercise the rights in other media and formats.\n" +
            "HOWEVER,\n" +
            "You must not:\n" +
            "\n" +
            "    impose any terms on the use to be made of the Work, the Adaptation or the Work as incorporated in a Collection that alter or restrict the terms of this Licence or the Applicable Licence, as applicable, or any rights granted under it or have the effect or intent of restricting the ability to exercise those rights;\n" +
            "    impose any digital rights management technology on the Work, the Adaptation or the Work as incorporated in a Collection that alters or restricts the terms of this Licence or any rights granted under it or has the effect or intent of restricting the ability to exercise those rights;\n" +
            "    sublicense the Work;\n" +
            "    falsely Attribute the Work to someone other than the Original Author;\n" +
            "    subject the Work to any derogatory treatment as defined in the Copyright Act 1994 provided that if the Licensor is the Original Author the Licensor will not enforce this sub-clause to the extent necessary to enable You to reasonably exercise Your right under clause 2.1 to make Adaptations but not otherwise.\n" +
            "\n" +
            "FINALLY,\n" +
            "You must:\n" +
            "\n" +
            "    make reference to this Licence or the Applicable Licence, as applicable, (by Uniform Resource Identifier (URI), spoken word or as appropriate to the media used) on all copies of the Work, Adaptations and Collections published, distributed, performed or otherwise disseminated or made available to the public by You;\n" +
            "    recognise the Licensor’s / Original Author’s right of Attribution (right to be identified) in any Work, Adaptation and Collection that You publish, distribute, perform or otherwise disseminate to the public and ensure that You credit the Licensor / Original Author as appropriate to the media used. You will however remove such a credit if requested by the Licensor/Original Author;\n" +
            "    not assert or imply any connection with sponsorship or endorsement by the Original Author or Licensor of You or Your use of the Work, without the separate, express prior written permission of the Original Author or Licensor; and\n" +
            "    to the extent reasonably practicable, keep intact all notices that refer to this Licence, in particular the URI, if any, that the Licensor specifies to be associated with the Work, unless such URI does not refer to the copyright notice or licensing information for the Work.\n" +
            "\n" +
            "Additional Provisions\n" +
            "2.2. Further licence from the Licensor\n" +
            "Each time You publish, distribute, perform or otherwise disseminate\n" +
            "\n" +
            "    the Work; or\n" +
            "    any Adaptation; or\n" +
            "    the Work as incorporated in a Collection\n" +
            "\n" +
            "the Licensor agrees to offer to the relevant third party making use of the Work (“User”) (in any of the alternatives set out above) a licence to use the Work on the same terms and conditions as granted to You hereunder.\n" +
            "2.3. Further licence from You\n" +
            "Each time You publish, distribute, perform or otherwise disseminate\n" +
            "\n" +
            "    an Adaptation; or\n" +
            "    an Adaptation as incorporated in a Collection\n" +
            "\n" +
            "You agree to offer to the User (in either of the alternatives set out above) a licence to use the Adaptation on any of the following premises and to comply with such licence:\n" +
            "\n" +
            "    a licence to the Adaptation on the same terms and conditions as the licence granted to You hereunder; or\n" +
            "    a later version of the licence granted to You hereunder with the same Licence Elements; or\n" +
            "    any other Creative Commons licence (whether the Unported or a jurisdiction licence) with the same Licence Elements.\n" +
            "    a Creative Commons Compatible Licence.\n" +
            "\n" +
            "2.4. This Licence does not affect any rights that the User may have under any applicable law, including fair use, fair dealing or any other legally recognised limitation or exception to copyright infringement.\n" +
            "2.5. All rights not expressly granted by the Licensor are hereby reserved.\n" +
            "2.6. The Licensor waives the right to collect royalties, whether individually or via a licensing body such as a collecting society, for any exercise by You of the rights granted under this Licence.\n" +
            "2.7. If the Licensor is the Original Author the Licensor waives its moral right to object to derogatory treatments of the Work to the extent necessary to enable You to reasonably exercise Your right under this Licence to make Adaptations but not otherwise. If the Licensor is not the Original Author the Work will still be subject to the moral rights of the Original Author.\n" +
            "3. Warranties and Disclaimer\n" +
            "Except as required by law or as otherwise agreed in writing between the parties, the Work is licensed by the Licensor on an \"as is\" and \"as available\" basis and without any warranty of any kind, either express or implied.\n" +
            "4. Limit of Liability\n" +
            "Subject to any liability which may not be excluded or limited by law the Licensor shall not be liable on any legal basis (including without limitation negligence) and hereby expressly excludes all liability for loss or damage howsoever and whenever caused to You.\n" +
            "5. Termination\n" +
            "The rights granted to You under this Licence shall terminate automatically upon any breach by You of the terms of this Licence. Individuals or entities who have received Adaptations or Collections from You under this Licence, however, will not have their Licences terminated provided such individuals or entities remain in full compliance with those Licences. Clauses 1, 3, 4, 5 and 6 will survive any termination of this Licence.\n" +
            "6. General\n" +
            "6.1. The validity or enforceability of the remaining terms of this agreement is not affected by the holding of any provision of it to be invalid or unenforceable.\n" +
            "6.2. This Licence constitutes the entire Licence Agreement between the parties with respect to the Work licensed here. There are no understandings, agreements or representations with respect to the Work not specified here. The Licensor shall not be bound by any additional provisions that may appear in any communication in any form.\n" +
            "6.3. A person who is not a party to this Licence shall have no rights under the Contracts (Privity) Act 1982 to enforce any of its terms.\n" +
            "7. On the role of Creative Commons\n" +
            "7.1. Creative Commons does not authorise either the Licensor or the User to use the trade mark “Creative Commons” or any related trade mark, including the Creative Commons logo, except to indicate that the Work is licensed under a Creative Commons Licence. Any permitted use has to be in compliance with the Creative Commons trade mark usage guidelines at the time of use of the Creative Commons trade mark. These guidelines may be found on the Creative Commons website or be otherwise available upon request from time to time. For the avoidance of doubt this trade mark restriction does not form part of this Licence.\n" +
            "7.2. Creative Commons Corporation does not profit financially from its role in providing this Licence and will not investigate the claims of any Licensor or user of the Licence.\n" +
            "7.3. One of the conditions that Creative Commons Corporation requires of the Licensor and You is an acknowledgement of its limited role and agreement by all who use the Licence that the Corporation is not responsible to anyone for the statements and actions of You or the Licensor or anyone else attempting to use or using this Licence.\n" +
            "7.4. Creative Commons Corporation is not a party to this Licence, and makes no warranty whatsoever in connection to the Work or in connection to the Licence, and in all events is not liable for any loss or damage resulting from the Licensor's or Your reliance on this Licence or on its enforceability.\n" +
            "7.5. USE OF THIS LICENCE MEANS THAT YOU AND THE LICENSOR EACH ACCEPTS THESE CONDITIONS IN SECTION 7.1, 7.2, 7.3, 7.4 AND EACH ACKNOWLEDGES CREATIVE COMMONS CORPORATION'S VERY LIMITED ROLE AS A FACILITATOR OF THE LICENCE FROM THE LICENSOR TO YOU.\n" +
            "Creative Commons Notice\n" +
            "Creative Commons is not a party to this Licence, and makes no warranty whatsoever in connection with the Work. Creative Commons will not be liable to You or any party on any legal theory for any damages whatsoever, including without limitation any general, special, incidental or consequential damages arising in connection to this Licence. Notwithstanding the foregoing two (2) sentences, if Creative Commons has expressly identified itself as the Licensor hereunder, it shall have all rights and obligations of Licensor.\n" +
            "Except for the limited purpose of indicating to the public that the Work is licensed under the CCPL, Creative Commons does not authorise the use by either party of the trademark \"Creative Commons\" or any related trademark or logo of Creative Commons without the prior written consent of Creative Commons. Any permitted use will be in compliance with Creative Commons' then-current trademark usage guidelines, as may be published on its website or otherwise made available upon request from time to time. For the avoidance of doubt, this trademark restriction does not form part of this Licence.\n" +
            "Creative Commons may be contacted at https://creativecommons.org/.\n");

    textFlowBox.getChildren().addAll(terms);
}

public void backButtonPush(){

}
}