//
//  SettingsViewController.h
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/7/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SettingsViewController : UIViewController
@property (strong, nonatomic) IBOutlet UISwitch *autoLoginSwitch;
@property (strong, nonatomic) IBOutlet UILabel *autoLoginLabel;
@property (strong, nonatomic) IBOutlet UIButton *autoLoginButton;

- (IBAction)switch_autologin:(id)sender;
- (IBAction)btn_autologin:(id)sender;

@end
